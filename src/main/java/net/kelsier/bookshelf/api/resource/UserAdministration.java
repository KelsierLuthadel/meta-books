package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kelsier.bookshelf.framework.error.exception.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/users")
@Produces({"application/json", "application/xml"})
public class UserAdministration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAdministration.class);

    public UserAdministration(/*final ConfigurationLoader configLoader*/) {
//        this.identityProvidersConfiguration = configLoader.loadConfiguration(IdentityProvidersConfiguration.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Login",
        tags = {"Authentication"},
        description = "Login",
        responses = {
            @ApiResponse(responseCode = "200")
        })

    public Response login(@Parameter(name = "username", required = true) @QueryParam("username") final String username,
                          @Parameter(name = "password", required = true)  @QueryParam("password") final String password) throws ResponseException {
        return Response.ok().build();
    }

}
