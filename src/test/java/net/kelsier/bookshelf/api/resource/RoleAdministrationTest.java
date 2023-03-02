package net.kelsier.bookshelf.api.resource;

import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.Role;
import net.kelsier.bookshelf.api.model.RoleModel;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUserRole;
import net.kelsier.bookshelf.framework.db.dao.users.RoleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class RoleAdministrationTest {
    private static final String MAX_STRING_LENGTH = new String(new char[31]).replace('\0', 'a');
    @Mock
    static final RoleDAO roleDAO = mock(RoleDAO.class);
    private static final ResourceExtension resources = ResourceExtension.builder()
        .addResource(new RoleAdministration(roleDAO))
        .build();

    @BeforeEach
    void setup() {
        doNothing().when(roleDAO).deleteById(any());
        when(roleDAO.insert(any())).thenReturn(1L);
        doNothing().when(roleDAO).update(any());

    }


    @Test
    void testGetRoles() {
        final List<DatabaseUserRole> roles = new ArrayList<>();
        roles.add(new DatabaseUserRole(1, "admin:c", "Create"));
        roles.add(new DatabaseUserRole(2, "admin:r", "Read"));
        roles.add(new DatabaseUserRole(3, "admin:u", "Update"));
        roles.add(new DatabaseUserRole(4, "admin:d", "Delete"));

        when(roleDAO.getAll()).thenReturn(roles);

        final Response post = resources.target("/api/1/roles").request().get();
        final List<RoleModel> models = post.readEntity((new GenericType<>(){}));

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
        for (int role = 0; role < roles.size(); role++){
            assertEquals(roles.get(role).getRole(), models.get(role).getRole(), "Role name should match");
            assertEquals(roles.get(role).getDescription(), models.get(role).getDescription(),"Role description should match");
        }
    }

    @Test
    void testGetRole() {
        final DatabaseUserRole role = new DatabaseUserRole(1, "admin:c", "Create");
        when(roleDAO.findById(1)).thenReturn(role);

        final Response post = resources.target("/api/1/roles/1").request().get();
        final RoleModel model = post.readEntity((new GenericType<>(){}));
        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
        assertEquals(role.getRole(), model.getRole(), "Role should match");
        assertEquals(role.getDescription(), model.getDescription(), "Description should match");
    }

    @Test
    void testCreateRole() {
        final Role role = new Role( "admin:c", "Create");
        when(roleDAO.findByName(anyString())).thenReturn(null);

        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(role));
        final RoleModel model = post.readEntity((new GenericType<>(){}));
        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
        assertEquals(role.getRoleName(), model.getRole(), "Role should match");
        assertEquals(role.getDescription(), model.getDescription(), "Description should match");
    }

    @Test
    void testUpdateRole() {
        final Role role = new Role( "admin:c", "Create Role");
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));
        final RoleModel model = post.readEntity((new GenericType<>(){}));
        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
        assertEquals(role.getRoleName(), model.getRole(), "Role should match");
        assertEquals(role.getDescription(), model.getDescription(), "Description should match");
    }

    @Test
    void testDeleteRole() {
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));
        final Response post = resources.target("/api/1/roles/1").request().delete();
        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
    }

    /**
     * Test for errors
     */

    @Test
    void testGetRole404() {
        when(roleDAO.findById(1)).thenReturn(null);
        final Response post = resources.target("/api/1/roles/1").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    @Test
    void testCreateRoleAlreadyExist() {
        when(roleDAO.findByName("admin:c")).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));
        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(new Role( "admin:c", "Create")));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), post.getStatus(),"Status should be 400 BAD REQUEST");
    }

    @Test
    void testUpdateRoleNameNotExist() {
        final Role role = new Role( "admin:c", "Create Role");
        when(roleDAO.findById(1)).thenReturn(null);

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    @Test
    void testUpdateRoleNameAlreadyExists() {
        final Role role = new Role( "admin:c", "Create Role");
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:d", "Create"));
        when(roleDAO.findByName("admin:c")).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), post.getStatus(),"Status should be 400 BAD REQUEST");
    }

    @Test
    void testDeleteRole404() {
        when(roleDAO.findById(1)).thenReturn(null);

        final Response post = resources.target("/api/1/roles/1").request().
            delete();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    /**
     * Test validation
     */

    @Test
    void testCreateRoleMinRoleLength() {
        final Role role = new Role( "ac", "Create");
        when(roleDAO.findByName(anyString())).thenReturn(null);

        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("roleName size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testCreateRoleMinRoleDescriptionLength() {
        final Role role = new Role( "admin:c", "as");
        when(roleDAO.findByName(anyString())).thenReturn(null);

        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("description size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testCreateRoleMaxRoleLength() {
        final Role role = new Role( MAX_STRING_LENGTH, "Create");
        when(roleDAO.findByName(anyString())).thenReturn(null);

        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("roleName size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testCreateRoleMaxRoleDescriptionLength() {
        final Role role = new Role( "admin:c", MAX_STRING_LENGTH);
        when(roleDAO.findByName(anyString())).thenReturn(null);

        final Response post = resources.target("/api/1/roles").request().
            post(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("description size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMinRoleLength() {
        final Role role = new Role( "ad", "Create Role");
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("roleName size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMinRoleDescriptionLength() {
        final Role role = new Role( "admin:c", "as");
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("description size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxRoleLength() {
        final Role role = new Role( MAX_STRING_LENGTH, "Create Role");
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("roleName size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxRoleDescriptionLength() {
        final Role role = new Role( "admin:c", MAX_STRING_LENGTH);
        when(roleDAO.findById(1)).thenReturn(new DatabaseUserRole(1, "admin:c", "Create"));

        final Response post = resources.target("/api/1/roles/1").request().
            put(Entity.json(role));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");
        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("description size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }


}