package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.User;
import net.kelsier.bookshelf.api.model.UserModel;
import net.kelsier.bookshelf.framework.db.DatabaseUser;
import net.kelsier.bookshelf.framework.db.DatabaseUserWithRoles;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * User Administration
     *
     * @param userDAO - User object represented in the database
     * @param roleDAO - Role objected represented in the database
     */
    public UserAdministration(final UserDAO userDAO, final RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
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
            tags = {"Authentication"},
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
                user.getEmail(), user.getEnabled(), user.getRoles())));

        return userModels;
    }

    /**
     * Get a user from the database
     * Restricted to the following role: admin:r
     *
     * @param id - An id representing a user in the database
     * @return HTTP response containing user details
     */
    @RolesAllowed({"admin:r"})
    @Path("{id}")
    @GET
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403"),
                    @ApiResponse(responseCode = "404"),
            })

    public UserModel getUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) {
        final DatabaseUser databaseUser = userDAO.get(id);
        final DatabaseUserWithRoles foo = userDAO.getWithRoles(id);

        if (null == databaseUser) {
            throw new NotFoundException();
        }

        return new UserModel(databaseUser.getId(), databaseUser.getUsername(), databaseUser.getFirstName(), databaseUser.getLastName(),
                databaseUser.getEmail(), databaseUser.getEnabled(), databaseUser.getRoles());
    }

    /**
     * Create a new user
     * Restricted to the following role: admin:c
     *
     * @param user - Object containing user details to persist to database
     *
     * @return HTTP response
     */
    @RolesAllowed({"admin:c"})
    @POST
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Add User",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            })
    public Response addUser(@Parameter(name = "user", required = true) @Valid final User user) {

        if (null == userDAO.find(user.getUsername())) {
            validateRoles(user.getRoles());
            final DatabaseUser databaseUser = new DatabaseUser(0, user.getUsername(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getEnabled(),
                    user.getPassword(), user.getRoles());
            userDAO.insert(databaseUser);
            return Response.status(Response.Status.CREATED).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private void validateRoles(List<Integer> roles) {
        roles.forEach((Integer roleId) -> {
            if (null == roleDAO.findById(roleId)) {
                throw new BadRequestException("Invalid role in request");
            }
        });
    }

    /**
     * Update user details
     * Restricted to the following role: admin:c
     *
     * @param id        - An id representing a user in the database
     * @param user - Object containing user details to persist to database
     * @return HTTP response
     */
    @RolesAllowed({"admin:u"})
    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            })

    public Response updateUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id,
                               @Parameter(name = "user", required = true) @Valid final User user) {
        final DatabaseUser databaseUser = userDAO.get(id);

        if (null == databaseUser) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // If the username has changed, ensure that there is no other user for the requested change
        if (!databaseUser.getUsername().equals(user.getUsername()) && null != userDAO.getUserId(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        validateRoles(user.getRoles());
        userDAO.update(mergeUserDetails(databaseUser, user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getEnabled(), user.getPassword(), user.getRoles()));
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private static DatabaseUser mergeUserDetails(@Valid final DatabaseUser databaseUser,
                                                 final String username, final String firstName, final String lastName,
                                                 final String email, final Boolean enabled, final String password,
                                                 final List<Integer> roles) {
        databaseUser.setUsername(username);

        if (null != firstName) {
            databaseUser.setFirstName(firstName);
        }
        if (null != lastName) {
            databaseUser.setLastName(lastName);
        }

        if (null != email) {
            databaseUser.setEmail(email);
        }

        if (null != enabled) {
            databaseUser.setEnabled(enabled);
        }

        if (null != password) {
            databaseUser.setPassword(password);
        }

        if (null != roles) {
            databaseUser.setRoles(roles);
        }

        return databaseUser;
    }

    /**
     * Delete a user
     * Restricted to the following role: admin:d
     *
     * @param id - The user ID
     * @return HTTP response
     */
    @RolesAllowed({"admin:d"})
    @Path("{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
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

        return Response.ok().build();
    }
}
