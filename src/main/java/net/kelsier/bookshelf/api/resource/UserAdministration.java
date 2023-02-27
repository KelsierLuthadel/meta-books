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
import net.kelsier.bookshelf.framework.error.exception.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotEmpty;
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


    public UserAdministration(final UserDAO userDAO, final RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @RolesAllowed("ADMIN")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public Response users() throws ResponseException {
        final List<User> users = userDAO.getAll();
        final List<UserModel> userModels = new ArrayList<>();

        users.forEach(user -> userModels.add(new UserModel(user.getId(),
                user.getUsername(), user.getFirstName(),user.getFirstName(),
                user.getEmail(), user.getEnabled(), user.getRoles())));

        return Response.ok(userModels).build();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Users",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public Response user(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) throws ResponseException {
        final User user = userDAO.get(id);
        final UserModel userModel = new UserModel(user.getId(), user.getUsername(), user.getFirstName(), user.getFirstName(),
                user.getEmail(), user.getEnabled(), user.getRoles());
        return Response.ok(userModel).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Authentication"},
            description = "Add User",
            responses = {
                    @ApiResponse(responseCode = "200")
            })
    //todo: path param?
    public Response addUser(@Parameter(name = "username", required = true) @QueryParam("username") final String username,
                            @Parameter(name = "fistName") @QueryParam("fistName") final String fistName,
                            @Parameter(name = "lastName") @QueryParam("lastName") final String lastName,
                            @Parameter(name = "email") @QueryParam("email") final String email,
                            @Parameter(name = "enabled", required = true) @QueryParam("enabled") final boolean enabled,
                            @Parameter(name = "password", required = true)  @QueryParam("password") final String password,
                            @Parameter(name = "roles", required = true) @QueryParam("roles") @NotEmpty final List<Integer> roles) throws ResponseException {

        if ( null == userDAO.find(username, password) ) {
            // todo: check role
            final User user = new User(0, username, fistName, lastName, email, enabled, password, roles);
            userDAO.insert(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // todo: update
    // todo: delete


}
