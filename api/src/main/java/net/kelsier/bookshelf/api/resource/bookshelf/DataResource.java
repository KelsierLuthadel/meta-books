
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
import net.kelsier.bookshelf.api.model.bookshelf.lookup.DataLookup;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static net.kelsier.bookshelf.api.db.tables.Table.DATA;

/**
 * API to retrieve ebook metadata from the database.
 */
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
public class DataResource {
    /**
     * Table type for matching
     */
    private static final Table TABLE_TYPE = DATA;

    /**
     * Database connection
     */
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection Connection to the database where book data is stored
     */
    public DataResource(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Search for ebook metadata
     * Restricted to the following roles: admin:r, user:r
     *
     * @param search A {@link Search} object, consisting of an {@link DataLookup} query and  {@link Pagination}
     * @return A paginated list of data
     *
     * <pre>Example request:{@code
     * {
     *   "query": {
     *     "field": "name",
     *     "operator": "EQ",
     *     "value": "1984 - George Orwell"
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
     *     "id": 64,
     *     "book": 59,
     *     "format": "EPUB",
     *     "uncompressedSize": 263729,
     *     "name": "1984 - George Orwell"
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
        summary = "Search for book details",
        tags = {"API"},
        description = "Get data for books",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
            @ApiResponse(responseCode = "404", description = "No data found"),
        })
    public List<Entity> authors(@Parameter(name="data", required = true) @NotNull @Valid final Search<DataLookup> search)  {
        return Connection.query(databaseConnection, TABLE_TYPE, search.getQuery(), search.getPagination());
    }

    /**
     * Get ebook metadata
     * @param bookDataId Book data ID
     * @return An object containing book metadata
     *
     * <pre>Example response:{@code
     * {
     *   "id": 64,
     *   "book": 59,
     *   "format": "EPUB",
     *   "uncompressedSize": 263729,
     *   "name": "1984 - George Orwell"
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
            summary = "Get data for a book",
            tags = {"API"},
            description = "Get data for a book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No data found"),
            })
    public Entity comment(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer bookDataId)  {
        final Entity entity =  Connection.get(databaseConnection, TABLE_TYPE, bookDataId);

        if (null != entity) {
            return entity;
        }

        throw new NotFoundException();
    }

}
