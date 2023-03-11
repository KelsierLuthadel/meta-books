package net.kelsier.bookshelf.framework.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUser;
import net.kelsier.bookshelf.framework.db.dao.users.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;
import net.kelsier.bookshelf.framework.db.model.users.User;
import net.kelsier.bookshelf.framework.db.model.users.UserModel;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User administration REST resources.
 *
 */
@Path("api/1/users")
@Produces({"application/json", "application/xml"})
@SecurityScheme(
        name = "basicAuth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "basicAuth")
)
public class UserAdministration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAdministration.class);

    private final UserDAO userDAO;

    private final RoleDAO roleDAO;

    private final @Valid EncryptionConfiguration cipherConfiguration;

    /**
     * User Administration
     *
     * @param userDAO User object represented in the database
     * @param roleDAO Role objected represented in the database
     */
    public UserAdministration(final UserDAO userDAO, final RoleDAO roleDAO, final EncryptionConfiguration cipherConfiguration) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.cipherConfiguration = cipherConfiguration;
    }

    /**
     * Get a list of users from the database
     * Restricted to the following role: admin:r
     *
     * @return HTTP response
     */
    @RolesAllowed({"admin:r"})
    @GET
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"User Administration", "Users"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            })
    public List<UserModel> users() {
        final List<DatabaseUser> users = userDAO.getAll();
        final List<UserModel> userModels = new ArrayList<>();

        users.forEach(user -> userModels.add(new UserModel(user.getId(),
                user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.isEnabled(), user.getRoles())));

        return userModels;
    }

    /**
     * Get a user from the database
     * Restricted to the following role: admin:r
     *
     * @param id An id representing a user in the database
     * @return HTTP response containing user details
     */
    @RolesAllowed({"admin:r"})
    @Path("{id}")
    @GET
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"User Administration", "Users"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403"),
                    @ApiResponse(responseCode = "404"),
            })

    public UserModel getUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) {
        final DatabaseUser databaseUser = userDAO.get(id);

        if (null == databaseUser) {
            throw new NotFoundException();
        }

        return new UserModel(databaseUser.getId(), databaseUser.getUsername(), databaseUser.getFirstName(), databaseUser.getLastName(),
                databaseUser.getEmail(), databaseUser.isEnabled(), databaseUser.getRoles());
    }

    /**
     * Create a new user
     * Restricted to the following role: admin:c
     *
     * @param user Object containing user details to persist to database
     *
     * @return HTTP response
     */
    @RolesAllowed({"admin:c"})
    @POST
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"User Administration", "Users"},
            description = "Add User",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            })
    public UserModel addUser(@Parameter(name = "user", required = true) @Valid final User user) {
        if (null != userDAO.find(user.getUsername())) {
            throw new BadRequestException();
        }

        validateRoles(user.getRoles());
        final DatabaseUser databaseUser = new DatabaseUser(0, user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.isEnabled(),
                new PasswordEncrypt(cipherConfiguration).encryptPassword(user.getPassword()), user.getRoles());
        long insertedId = userDAO.insert(databaseUser);

        final String createdMessage = MessageFormat.format("User {0} created", user.getUsername());
        LOGGER.info(createdMessage);

        return new UserModel(Math.toIntExact(insertedId), user.getUsername(), user.getFirstName(),user.getLastName(),
            user.getEmail(), user.isEnabled(), user.getRoles());
    }

    /**
     * Update user details
     * Restricted to the following role: admin:c
     *
     * @param id   An id representing a user in the database
     * @param user Object containing user details to persist to database
     * @return HTTP response
     */
    @RolesAllowed({"admin:u"})
    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"User Administration", "Users"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            })

    public UserModel updateUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id,
                               @Parameter(name = "user", required = true) @Valid final User user) {
        final DatabaseUser databaseUser = userDAO.get(id);

        if (null == databaseUser) {
            throw new NotFoundException();
        }

        // If the username has changed, ensure that there is no other user for the requested change
        if (!databaseUser.getUsername().equals(user.getUsername()) && null != userDAO.getUserId(user.getUsername())) {
            throw new BadRequestException();
        }

        validateRoles(user.getRoles());
        userDAO.update(new DatabaseUser(
            databaseUser.getId(),user.getUsername(), user.getFirstName(),user.getLastName(),
            user.getEmail(), user.isEnabled(), user.getPassword(),user.getRoles()));

        final String createdMessage = MessageFormat.format("User {0} updated", user.getUsername());
        LOGGER.info(createdMessage);

        return new UserModel(databaseUser.getId(),user.getUsername(), user.getFirstName(),user.getLastName(),
            user.getEmail(), user.isEnabled(), user.getRoles());
    }

    /**
     * Delete a user
     * Restricted to the following role: admin:d
     *
     * @param id The user ID
     * @return HTTP response
     */
    @RolesAllowed({"admin:d"})
    @Path("{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"User Administration", "Users"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403"),
                    @ApiResponse(responseCode = "404")
            })

    public Response deleteUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) {
        final DatabaseUser user = userDAO.get(id);

        if (null == user) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userDAO.deleteById(id);

        final String createdMessage = MessageFormat.format("User {0} deleted", user.getUsername());
        LOGGER.info(createdMessage);

        return Response.ok().build();
    }

    /**
     * Validate that roles exist
     *
     * @param roles A list of roles to validate
     */
    private void validateRoles(final List<Integer> roles) {
        roles.forEach((Integer roleId) -> {
            if (null == roleDAO.findById(roleId)) {
                throw new BadRequestException("Invalid role in request");
            }
        });
    }
}
