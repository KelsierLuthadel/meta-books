package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.DataLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.db.dao.DataDAO;
import net.kelsier.bookshelf.api.db.model.BookData;
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

@Path("api/1/bookshelf/data")
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
public class Data {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Data(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     *
     * Restricted to the following roles: admin:r, user:r
     *
     * @return
     */
    @POST
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Search for book details",
        tags = {"Bookshelf"},
        description = "Get data for books",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No data found"),
        })
    public List<BookData> authors(@Parameter(name="data", required = true) @NotNull @Valid final Search<DataLookup> search)  {
        if (null == search.getQuery()) {
            return databaseConnection.onDemand(DataDAO.class).find(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection());
        } else {
            switch(search.getQuery().getField()) {
                case "format":
                case "name":
                    return databaseConnection.onDemand(DataDAO.class).find(
                            search.getQuery().getLookupValue(),
                            search.getQuery().getField(),
                            search.getQuery().getOperator().getLabel(),
                            search.getPagination().getLimit(),
                            search.getPagination().getStart(),
                            search.getPagination().getSort().getField(),
                            search.getPagination().getSort().getDirection()
                    );
                case "uncompressed_size":
                    return databaseConnection.onDemand(DataDAO.class).find(
                            Integer.parseInt(search.getQuery().getValue()),
                            search.getQuery().getField(),
                            search.getQuery().getOperator().getLabel(),
                            search.getPagination().getLimit(),
                            search.getPagination().getStart(),
                            search.getPagination().getSort().getField(),
                            search.getPagination().getSort().getDirection()
                    );
            }
            throw new BadRequestException();
        }
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get data for a book",
            tags = {"Bookshelf"},
            description = "Get data for a book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No data found"),
            })
    public BookData comment(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer bookDataId)  {
        return databaseConnection.onDemand(DataDAO.class).get(bookDataId);
    }

}
