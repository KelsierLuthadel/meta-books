package net.kelsier.bookshelf.framework.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUser;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;
import net.kelsier.bookshelf.framework.db.model.users.LoginModel;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/login")
@Produces("application/json")
public class Login {
    private final UserDAO userDAO;

    private final @Valid EncryptionConfiguration cipherConfiguration;



    public Login(final UserDAO userDAO, final EncryptionConfiguration cipherConfiguration) {
        this.userDAO = userDAO;
        this.cipherConfiguration = cipherConfiguration;
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

    public Response login(@Parameter(name = "login", required = true) final @Valid LoginModel loginModel) {
        final DatabaseUser user = userDAO.find(loginModel.getUsername());

        if (user.isEnabled() && new PasswordEncrypt(cipherConfiguration).checkPassword(loginModel.getPassword(), user.getPassword())) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
