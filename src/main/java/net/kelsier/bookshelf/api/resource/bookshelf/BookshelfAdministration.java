package net.kelsier.bookshelf.api.resource.bookshelf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.migrations.MigrateSQLite;
import net.kelsier.bookshelf.migrations.exception.MigrationException;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/bookshelf/admin")
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
public class BookshelfAdministration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookshelfAdministration.class);
    private final Jdbi databaseConnection;

    /**
     *
     * @param
     */
    public BookshelfAdministration(final Jdbi databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @POST
    @RolesAllowed({"admin:c"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Bookshelf Admin"},
            description = "Migrate Database",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public Response migrateDatabase(@Parameter(name = "database", required = true) final String database) {
        try {
            LOGGER.info("Migrating database");
            final MigrateSQLite migrate = new MigrateSQLite(databaseConnection,database);
            migrate.migrate();
            LOGGER.info("Database migrated");
        } catch (MigrationException e) {
            throw new InternalServerErrorException(e);
        }

        return Response.ok().build();
    }

    @DELETE
    @RolesAllowed({"admin:d"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Bookshelf Admin"},
            description = "Migrate Database",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public Response clearDatabase() {
        try {
            MigrateSQLite migrate = new MigrateSQLite(databaseConnection,null);
            migrate.drop();
        } catch (MigrationException e) {
            throw new InternalServerErrorException(e);
        }

        return Response.ok().build();
    }

}
