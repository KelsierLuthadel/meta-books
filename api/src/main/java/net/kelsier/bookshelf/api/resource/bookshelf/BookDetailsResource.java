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
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static net.kelsier.bookshelf.api.db.tables.Table.BOOK_DETAILS;

/**
 * API to retrieve a book with associated metadata from the database.
 */@Path("api/1/bookshelf")
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
public class BookDetailsResource {
    /**
     * Table type for matching
     */
    private static final Table TABLE_TYPE = BOOK_DETAILS;

    /**
     * Database connection
     */
    private final Jdbi databaseConnection;

    /**
     * Bookshelf REST resource
     *
     * @param databaseConnection Connection to the database where book data is stored
     */
    public BookDetailsResource(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Get full book details
     * @param bookId Book ID
     * @return An object containing book details
     *
     * <pre>Example response:{@code
     * {
     *   "id": 1,
     *   "title": "The Book Title",
     *   "author": "Author Name",
     *   "series": "Book series",
     *   "seriesIndex": 5,
     *   "publisher": "Publisher",
     *   "isbn": "1234567890",
     *   "language": "eng",
     *   "format": "EPUB",
     *   "size": 494805,
     *   "hasCover": true,
     *   "dateAdded": "2015-11-15",
     *   "published": "2006-08-22",
     *   "lastModified": "2023-02-23",
     *   "path": "path/book",
     *   "comments": "Book comments"
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
            summary = "Get Book details",
            tags = {"Bookshelf"},
            description = "Get Book details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Not allowed to view this resource"),
                    @ApiResponse(responseCode = "404", description = "No tags found"),
            })
    public Entity get(@Parameter(name="id", required = true) @NotNull @PathParam("id") final Integer bookId)  {
        final Entity entity = Connection.get(databaseConnection, TABLE_TYPE, bookId);

        if (null != entity) {
            return entity;
        }

        throw new NotFoundException();
    }

}
