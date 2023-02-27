package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.UserModel;
import net.kelsier.bookshelf.framework.db.User;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static net.kelsier.bookshelf.framework.error.response.RegexPatterns.OWASP_EMAIL_REGEX;
import static net.kelsier.bookshelf.framework.error.response.RegexPatterns.PASSWORD_REGEX;

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
    public Response users() {
        final List<User> users = userDAO.getAll();
        final List<UserModel> userModels = new ArrayList<>();

        users.forEach(user -> userModels.add(new UserModel(user.getId(),
                user.getUsername(), user.getFirstName(),user.getFirstName(),
                user.getEmail(), user.getEnabled(), user.getRoles())));

        return Response.ok(userModels).build();
    }

    /**
     * Get a user from the database
     * Restricted to the following role: admin:r
     *
     * @param id - An id representing a user in the database
     *
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

    public Response getUser(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) {
        final User user = userDAO.get(id);

        if (null == user) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        @Valid final UserModel userModel = new UserModel(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getEnabled(), user.getRoles());
        return Response.ok(userModel).build();
    }

    /**
     * Create a new user
     * Restricted to the following role: admin:c
     *
     * @param username - A unique username
     * @param firstName - The user's first name
     * @param lastName - The user's last name
     * @param email - The user's email address
     * @param enabled - A flag determining if the account is enabled
     * @param password - The user's password
     * @param roles - A list of role IDs associated with the user, this is mapped to {@link RoleDAO}
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
    public Response addUser(@Parameter(name = "username", required = true) @QueryParam("username") final String username,
                            @Parameter(name = "firstName") @QueryParam("firstName") final String firstName,
                            @Parameter(name = "lastName") @QueryParam("lastName") final String lastName,
                            @Valid @Parameter(name = "email") @QueryParam("email") @Pattern(regexp = OWASP_EMAIL_REGEX) final String email,
                            @Parameter(name = "enabled", required = true) @QueryParam("enabled")  final boolean enabled,
                            @Valid @Parameter(name = "password", required = true)  @QueryParam("password") @Pattern(regexp = PASSWORD_REGEX) final String password,
                            @Parameter(name = "roles", required = true) @QueryParam("roles") @NotEmpty final List<Integer> roles) {

        if ( null == userDAO.find(username, password) ) {
            validateRoles(roles);
            final User user = new User(0, username, firstName, lastName, email, enabled, password, roles);
            userDAO.insert(user);
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
     * @param id - An id representing a user in the database
     * @param username - A unique username
     * @param firstName - The user's first name
     * @param lastName - The user's last name
     * @param email - The user's email address
     * @param enabled - A flag determining if the account is enabled
     * @param password - The user's password
     * @param roles - A list of role IDs associated with the user, this is mapped to {@link RoleDAO}
     *
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
                               @Parameter(name = "username") @QueryParam("username") final String username,
                               @Parameter(name = "fistName") @QueryParam("fistName") final String firstName,
                               @Parameter(name = "lastName") @QueryParam("lastName") final String lastName,
                               @Parameter(name = "email") @QueryParam("email") @Pattern(regexp = OWASP_EMAIL_REGEX) final String email,
                               @Parameter(name = "enabled") @QueryParam("enabled") final Boolean enabled,
                               @Parameter(name = "password")  @QueryParam("password") @Pattern(regexp = PASSWORD_REGEX) final String password,
                               @Parameter(name = "roles") @QueryParam("roles") final List<Integer> roles) {
        final User user = userDAO.get(id);

        if (null == user) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // If the username has changed, ensure that there is no other user for the requested change
        if (!user.getUsername().equals(username) && null != userDAO.getUserId(username)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        validateRoles(roles);
        userDAO.update(mergeUserDetails(user, username ,firstName, lastName, email, enabled, password, roles));
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private static User mergeUserDetails(@Valid final User user,
                          final String username, final String firstName, final String lastName,
                          final String email, final Boolean enabled, final String password,
                          final List<Integer> roles) {
        user.setUsername(username);

        if (null != firstName) {
            user.setFirstName(firstName);
        }
        if (null != lastName) {
            user.setLastName(lastName);
        }

        if (null != email) {
            user.setEmail(email);
        }

        if (null != enabled) {
            user.setEnabled(enabled);
        }

        if (null != password) {
            user.setPassword(password);
        }

        if (null != roles) {
            user.setRoles(roles);
        }

        return user;
    }

    /**
     * Delete a user
     * Restricted to the following role: admin:d
     *
     * @param id - The user ID
     *
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
        final User user = userDAO.get(id);

        if (null == user) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userDAO.deleteById(id);

        return Response.ok().build();
    }
}
