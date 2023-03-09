
/*
 * Copyright (c) 2023. Kelsier Luthadel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.api.db.connection.Connection;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.db.tables.Table;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.SeriesLookup;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static net.kelsier.bookshelf.api.db.tables.Table.*;

/**
 * API to retrieve book series information from the database.
 */
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
public class BookSeriesResource {
    /**
     * Table type for matching
     */
    private static final Table TABLE_TYPE = SERIES;

    /**
     * Database connection
     */
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection Connection to the database where book data is stored
     */
    public BookSeriesResource(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Search for series
     * Restricted to the following roles: admin:r, user:r
     *
     * @param search A {@link Search} object, consisting of an {@link SeriesLookup} query and  {@link Pagination}
     * @return A paginated list of series
     *
     * <pre>Example request:{@code
     * {
     *   "query": {
     *     "field": "name",
     *     "operator": "LIKE",
     *     "value": "series"
     *   },
     *   "pagination": {
     *     "start": 0,
     *     "limit": 10,
     *     "sort": {
     *       "field": "name",
     *       "direction": "asc"
     *     }
     *   }
     * }
     * }</pre>
     *
     * <pre>Example response:{@code
     * [
     *   {
     *     "id": 97,
     *     "name": "Seventh Series",
     *     "sort": "Seventh Series"
     *   },
     *   {
     *     "id": 384,
     *     "name": "The Series",
     *     "sort": "Series, The"
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
        summary = "Search for a series",
        tags = {"API"},
        description = "Search for a series",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No series found"),
        })
    public List<Entity> series(@Parameter(name="data", required = true) @NotNull @Valid final Search<SeriesLookup> search)  {
        return Connection.query(databaseConnection, TABLE_TYPE, search.getQuery(), search.getPagination());
    }

    /**
     * Get Book series details
     * @param seriesId Series ID
     * @return An object containing series details
     *
     * <pre>Example response:{@code
     * {
     *   "id": 384,
     *   "name": "The Series",
     *   "sort": "Series, The"
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
            summary = "Get series details",
            tags = {"API"},
            description = "Get series details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No series found"),
            })
    public Entity series(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer seriesId)  {
        final Entity entity = Connection.get(databaseConnection, TABLE_TYPE, seriesId);

        if (null != entity) {
            return entity;
        }

        throw new NotFoundException();
    }

}
