package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.model.bookshelf.LanguageLookup;
import net.kelsier.bookshelf.api.model.bookshelf.PublisherLookup;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.LanguageDAO;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.PublisherDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Language;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Publisher;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/1/bookshelf/publishers")
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
public class Publishers {
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection - Connection to the database where book data is stored
     */
    public Publishers(final Jdbi databaseConnection) {
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
        summary = "Search publishers",
        tags = {"Bookshelf"},
        description = "Search publishers",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No publishers found"),
        })
    public List<Publisher> authors(@Parameter(name="data", required = true) @NotNull @Valid final Search<PublisherLookup> search)  {
        if (null == search.getLookup()) {
            return databaseConnection.onDemand(PublisherDAO.class).find(
                    search.getPagination().getLimit(),
                    search.getPagination().getStart(),
                    search.getPagination().getSort().getField(),
                    search.getPagination().getSort().getDirection()
            );
        } else {
            return databaseConnection.onDemand(PublisherDAO.class).find(
                    search.getLookup().getLookupValue(),
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
            summary = "Get publisher details",
            tags = {"Bookshelf"},
            description = "Get publisher details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No publisher found"),
            })
    public Publisher comment(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer publisherId)  {
        return databaseConnection.onDemand(PublisherDAO.class).get(publisherId);
    }

}