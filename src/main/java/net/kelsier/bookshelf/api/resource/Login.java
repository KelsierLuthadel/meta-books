package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kelsier.bookshelf.framework.db.User;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/login")
@Produces({"application/json", "application/xml"})
public class Login {
    private static final Logger LOGGER = LoggerFactory.getLogger(Login.class);
    private final UserDAO userDAO;

    public Login(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Login",
        tags = {"Login"},
        description = "Login",
        responses = {
            @ApiResponse(responseCode = "200")
        })

    public Response login(@Parameter(name = "username", required = true) @QueryParam("username") final String username,
                          @Parameter(name = "password", required = true)  @QueryParam("password") final String password) {
        final User user = userDAO.find(username, password);
        if (null != user && user.getEnabled()) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
