package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.AuthorLookup;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.db.dao.AuthorDAO;
import net.kelsier.bookshelf.api.db.model.Author;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * API to retrieve authors from the database.
 * Authors are stored in the following schema:
 * <p>
 * {@code public.authors (id SERIAL PRIMARY KEY, name TEXT NOT NULL , sort TEXT)}
 * </p>
 */
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
public final class Authors {
    private final Jdbi databaseConnection;

    /**
     * Constructor
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Authors(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Search for authors
     * Restricted to the following roles: admin:r, user:r
     *
     * @param search A {@link Search} object, consisting of an {@link AuthorLookup} query and  {@link Pagination}
     * @return A paginated list of authors
     *
     * <pre>Example request:{@code
     * "lookup": {
     *     "field": "name",
     *     "operator": "LIKE",
     *     "value": "Stephen King"
     *   },
     *   "pagination": {
     *     "start": 0,
     *     "limit": 10,
     *     "sort": {
     *       "field": "name",
     *       "direction": "asc"
     *     }
     *   }
     * }</pre>
     *
     * <pre>Example response:{@code
     * [
     *   {
     *     "id": 1,
     *     "name": "Stephen King",
     *     "sort": "King, Stephen"
     *   },
     *   {
     *     "id": 2,
     *     "name": "Stephen Kingston",
     *     "sort": "Kingston, Stephen"
     *   }
     * ]
     * }</pre>
     */
    @POST
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Search for authors",
        tags = {"Bookshelf"},
        description = "Search for authors",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No authors found"),
        })
    public List<Author> authors(@Parameter(name="search", required = true) @NotNull @Valid final Search<AuthorLookup> search)  {
        if (null == search.getQuery()) {
            return databaseConnection.onDemand(AuthorDAO.class).get(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        } else {
            return databaseConnection.onDemand(AuthorDAO.class).find(
                    search.getQuery().getLookupValue(),
                    search.getQuery().getField(),
                    search.getQuery().getOperator().getLabel(),
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        }
    }

    /**
     * Get Author details
     * @param authorId - Author ID
     * @return - An object containing author details
     * 
     * <pre>Example response:{@code
     * {
     *   "id": 1,
     *   "name": "Stephen King",
     *   "sort": "King, Stephen"
     * }
     * }</pre>
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get author details",
            tags = {"Bookshelf"},
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
