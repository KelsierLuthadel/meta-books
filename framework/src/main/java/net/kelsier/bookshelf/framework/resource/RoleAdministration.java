package net.kelsier.bookshelf.framework.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUserRole;
import net.kelsier.bookshelf.framework.db.dao.users.RoleDAO;
import net.kelsier.bookshelf.framework.db.model.users.Role;
import net.kelsier.bookshelf.framework.db.model.users.RoleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Path("api/1/roles")
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
public class RoleAdministration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleAdministration.class);
    private final RoleDAO roleDAO;

    /**
     *
     * @param roleDAO
     */
    public RoleAdministration(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @GET
    @RolesAllowed({"admin:r"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get list of roles",
            tags = {"Roles"},
            description = "Get Roles",
            responses = {
                @ApiResponse(responseCode = "200")
            })

    public List<RoleModel> getRoles()  {
        final List<DatabaseUserRole> roles = roleDAO.getAll();
        final List<RoleModel> models = new ArrayList<>();

        roles.forEach((DatabaseUserRole userRole) ->
            models.add(new RoleModel(userRole.getId(), userRole.getRole(), userRole.getDescription()))
        );

        return models;
    }

    /**
     * Get a role
     *
     * @param id - id of the role to get
     * @return - An object representing the role
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"admin:r"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get role details",
            tags = {"Roles"},
            description = "Get Role",
            responses = {
                @ApiResponse(responseCode = "200")
            })
    public RoleModel getRole(@Parameter(name = "id", required = true) @PathParam("id") final Integer id)  {
        final DatabaseUserRole role = roleDAO.findById(id);
        if (null == role) {
            throw new NotFoundException();
        }
        return new RoleModel(id, role.getRole(), role.getDescription());
    }


    @POST
    @RolesAllowed({"admin:c"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create new role",
            tags = {"Roles"},
            description = "Create role",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public RoleModel addRole(@Parameter(name = "role", required = true) @Valid final Role role) {
        if (null != roleDAO.findByName(role.getRoleName())) {
            throw new BadRequestException();
        }

        long insertedId = roleDAO.insert(new DatabaseUserRole(0,role.getRoleName(), role.getDescription()));
        final String message = MessageFormat.format("Role {0} created", role.getRoleName());
        LOGGER.info(message);
        return new RoleModel(Math.toIntExact(insertedId), role.getRoleName(), role.getDescription());
    }

    /**
     * Update role details
     *
     * @param id - Role id to update
     * @param role - Role details to update
     * @return - HTTP 200 when successful
     */
    @PUT
    @RolesAllowed({"admin:u"})
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update role details",
            tags = {"Roles"},
            description = "Update role",
            responses = {
                @ApiResponse(responseCode = "200")
            })
    public RoleModel updateRole(@Parameter(name = "id", required = true) @PathParam("id") final Integer id,
                                @Parameter(name = "role", required = true) @Valid final Role role) {
        DatabaseUserRole existingRole = roleDAO.findById(id);
        if (null == existingRole) {
            throw new NotFoundException();
        }

        // If the role has changed, ensure that there is no other role for the requested change
        if (!existingRole.getRole().equals(role.getRoleName()) && null != roleDAO.findByName(role.getRoleName())) {
            throw new BadRequestException();
        }

        roleDAO.update(new DatabaseUserRole(id,role.getRoleName(), role.getDescription()));

        final String message = MessageFormat.format("Role {0} updated", role.getRoleName());
        LOGGER.info(message);

        return new RoleModel(id,role.getRoleName(), role.getDescription());
    }

    /**
     * Delete a given role
     *
     * @param id - Role id
     * @return - HTTP 200 response when the role was deleted
     */
    @DELETE
    @RolesAllowed({"admin:d"})
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete role",
            tags = {"Roles"},
            description = "Delete role",
            responses = {
                @ApiResponse(responseCode = "200")
            })
    public Response deleteRole(@Parameter(name = "id", required = true) @PathParam("id") final Integer id) {
        final DatabaseUserRole role = roleDAO.findById(id);
        if (null == role) {
            throw new NotFoundException();
        }

        roleDAO.deleteById(id);
        final String message = MessageFormat.format("Role {0} deleted", role.getRole());
        LOGGER.info(message);
        return Response.ok().build();
    }
}
