package net.kelsier.bookshelf.api.resource;

import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.framework.auth.AccessTokenPrincipal;
import net.kelsier.bookshelf.framework.error.exception.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/books")
@Produces({"application/json", "application/xml"})
//@SecurityScheme(
//        name="Authorization",
//        type=SecuritySchemeType.HTTP,
//        scheme="Bearer",
//        bearerFormat="JWT"
//)
//@SecurityScheme(
//        name = "basicAuth", // can be set to anything
//        type = SecuritySchemeType.HTTP,
//        scheme = "basic"
//)
//@OpenAPIDefinition(
//        security = @SecurityRequirement(name = "Secured")
//)
public class Bookshelf {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bookshelf.class);

    public Bookshelf(/*final ConfigurationLoader configLoader*/) {
//        this.identityProvidersConfiguration = configLoader.loadConfiguration(IdentityProvidersConfiguration.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get Books",
        tags = {"Bookshelf"},
        description = "Get a list of books",
        responses = {
            @ApiResponse(responseCode = "200")
        })

    public Response books(@Auth AccessTokenPrincipal tokenPrincipal) throws ResponseException {
        return Response.ok().build();
    }

}
