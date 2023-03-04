package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.AuthorLookup;
import net.kelsier.bookshelf.api.model.bookshelf.BookLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.AuthorDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Author;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf/authors")
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
public class Authors {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Authors(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     *
     * Restricted to the following roles: admin:r, user:r
     *
     * @return A paginated list of authors
     */
    @POST
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get authors",
        tags = {"Authors"},
        description = "Get a list of authors",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No authors found"),
        })
    public List<Author> authors(@Parameter(name="search", required = true) @NotNull @Valid final Search<AuthorLookup> search)  {
        if (null == search.getLookup()) {
            return databaseConnection.onDemand(AuthorDAO.class).get(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart());
        } else {
            return databaseConnection.onDemand(AuthorDAO.class).find(
                    search.getLookup().getWildcardValue(),
                    search.getLookup().getField(),
                    search.getPagination().getLimit(),
                    search.getPagination().getStart());
        }
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get an author",
            tags = {"Authors"},
            description = "Get author details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No author found"),
            })
    public Author author(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer authorId)  {
        return databaseConnection.onDemand(AuthorDAO.class).get(authorId);
    }

}
