package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.Search;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.BookDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf")
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
public class Bookshelf {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Bookshelf(final Jdbi databaseConnection) {
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
        summary = "Get Books",
        tags = {"Bookshelf"},
        description = "Get a list of books",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No books found"),
        })
    public List<Book> books(@Parameter(name="search", required = true) @NotNull final Search search)  {
        return databaseConnection.onDemand(BookDAO.class).findByTitle(
                search.getValue(),
                search.getPagination().getLimit(),
                search.getPagination().getStart());
    }

}
