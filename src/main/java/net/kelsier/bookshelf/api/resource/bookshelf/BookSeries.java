package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.RatingLookup;
import net.kelsier.bookshelf.api.model.bookshelf.SeriesLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.RatingDAO;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.SeriesDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Rating;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Series;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf/series")
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
public class BookSeries {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public BookSeries(final Jdbi databaseConnection) {
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
        summary = "Search for a series",
        tags = {"Bookshelf"},
        description = "Search for a series",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No series found"),
        })
    public List<Series> series(@Parameter(name="data", required = true) @NotNull @Valid final Search<SeriesLookup> search)  {
        if (null == search.getLookup()) {
            return databaseConnection.onDemand(SeriesDAO.class).find(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart());
        } else {
            return databaseConnection.onDemand(SeriesDAO.class).find(
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
            summary = "Get series details",
            tags = {"Bookshelf"},
            description = "Get series details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No series found"),
            })
    public Series series(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer seriesId)  {
        return databaseConnection.onDemand(SeriesDAO.class).get(seriesId);
    }

}
