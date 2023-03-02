package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kelsier.bookshelf.api.model.LoginModel;
import net.kelsier.bookshelf.framework.db.DatabaseUser;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jasypt.util.password.PasswordEncryptor;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/login")
@Produces("application/json")
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

    public Response login(@Parameter(name = "login", required = true) final LoginModel loginModel) {
        final DatabaseUser user = userDAO.find(loginModel.getUsername());

        if (user.getEnabled() && new PasswordEncrypt().checkPassword(loginModel.getPassword(), user.getPassword())) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
