package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.db.dao.BookDAO;
import net.kelsier.bookshelf.api.db.model.Book;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf/books")
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
public final class Books {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection Connection to the database where book data is stored
     */
    public Books(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Get a list of books in the database based on the title
     * Restricted to the following roles: admin:r, user:r
     *
     * @return A paginated list of books
     */
    @POST
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Search for books",
        tags = {"Bookshelf"},
        description = "Get a list of books",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No books found"),
        })
    public List<Book> books(@Parameter(name="search", required = true) @NotNull @Valid final Search<BookLookup> search)  {
        if (null == search.getQuery()) {
            return databaseConnection.onDemand(BookDAO.class).get(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        } else {
            switch(search.getQuery().getField()) {
                case "title":
                case "isbn":
                    return databaseConnection.onDemand(BookDAO.class).find(
                            search.getQuery().getLookupValue(),
                            search.getQuery().getField(),
                            search.getQuery().getOperator().getLabel(),
                            search.getPagination().getLimit(),
                            search.getPagination().getStart(),
                            search.getPagination().getSort().getField(),
                            search.getPagination().getSort().getDirection()
                    );
                case "has_cover":
                default:
                    return databaseConnection.onDemand(BookDAO.class).find(
                            Boolean.parseBoolean(search.getQuery().getValue()),
                            search.getQuery().getField(),
                            search.getQuery().getOperator().getLabel(),
                            search.getPagination().getLimit(),
                            search.getPagination().getStart(),
                            search.getPagination().getSort().getField(),
                            search.getPagination().getSort().getDirection()
                    );
            }
        }
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get book details",
            tags = {"Bookshelf"},
            description = "Get book details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No books found"),
            })
    public Book book(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer bookId)  {
        return databaseConnection.onDemand(BookDAO.class).get(bookId);
    }

}
