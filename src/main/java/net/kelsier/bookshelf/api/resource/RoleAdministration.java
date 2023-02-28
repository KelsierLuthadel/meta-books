package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kelsier.bookshelf.framework.db.DatabaseUserRole;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/roles")
@Produces({"application/json", "application/xml"})
public class RoleAdministration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleAdministration.class);
    private final RoleDAO roleDAO;


    public RoleAdministration(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        tags = {"Roles"},
        description = "Get Roles",
        responses = {
            @ApiResponse(responseCode = "200")
        })

    public Response getRoles()  {
        return Response.ok(roleDAO.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"Roles"},
            description = "Create role",
            responses = {
                    @ApiResponse(responseCode = "200")
            })

    public Response addRole(@Parameter(name = "role", required = true) @QueryParam("role") final String role,
                          @Parameter(name = "description", required = true)  @QueryParam("description") final String description) {

        //todo: check exists
        roleDAO.insert(new DatabaseUserRole(0,role, description));
        return Response.ok().build();
    }

    // todo: update
    // todo: delete

}
