package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.RatingLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.RatingDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Rating;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf/ratings")
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
public class Ratings {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Ratings(final Jdbi databaseConnection) {
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
        summary = "Search ratings",
        tags = {"Bookshelf"},
        description = "Search ratings",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No ratings found"),
        })
    public List<Rating> ratings(@Parameter(name="data", required = true) @NotNull @Valid final Search<RatingLookup> search)  {
        if (null == search.getLookup()) {
            return databaseConnection.onDemand(RatingDAO.class).find(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        } else {
            return databaseConnection.onDemand(RatingDAO.class).find(
                    Integer.parseInt(search.getLookup().getValue()),
                    search.getLookup().getField(),
                    search.getLookup().getOperator().getLabel(),
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        }
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r", "user:r"})
    @Valid
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get rating details",
            tags = {"Bookshelf"},
            description = "Get rating details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No rating found"),
            })
    public Rating rating(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer ratingId)  {
        return databaseConnection.onDemand(RatingDAO.class).get(ratingId);
    }

}
