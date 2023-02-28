package net.kelsier.bookshelf.framework.db;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.User;
import net.kelsier.bookshelf.api.model.UserModel;
import net.kelsier.bookshelf.api.resource.UserAdministration;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class UserTest {
    @Mock static final UserDAO userDAO = mock(UserDAO.class);
    @Mock static final RoleDAO roleDAO = mock(RoleDAO.class);
    private static final ResourceExtension resources = ResourceExtension.builder()
        .addResource(new UserAdministration(userDAO, roleDAO))
        .build();


    @Test
    void testGetUsers() {
        final List<DatabaseUser> users = new ArrayList<>();

        users.add(new DatabaseUser(1, "username", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        when(userDAO.getAll()).thenReturn(users);

        final Response post = resources.target("/api/1/users").request().get();
        final List<UserModel> userModel = post.readEntity((new GenericType<List<UserModel>>() {}));

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus());
        assertEquals(users.get(0).getUsername(), userModel.get(0).getUsername());
        assertEquals(users.get(0).getFirstName(), userModel.get(0).getFirstName());
        assertEquals(users.get(0).getLastName(), userModel.get(0).getLastName());
        assertEquals(users.get(0).getEmail(), userModel.get(0).getEmail());
        assertEquals(users.get(0).getEnabled(), userModel.get(0).getEnabled());
        assertEquals(users.get(0).getRoles().size(), userModel.get(0).getRoles().size());
        assertEquals(users.get(0).getRoles().get(0), userModel.get(0).getRoles().get(0));
        assertEquals(users.get(0).getRoles().get(1), userModel.get(0).getRoles().get(1));
        assertEquals(users.get(0).getRoles().get(2), userModel.get(0).getRoles().get(2));
        assertEquals(users.get(0).getRoles().get(3), userModel.get(0).getRoles().get(3));
    }

    @Test
    void testGetUser() {
        final DatabaseUser user = new DatabaseUser(1, "username", "first", "last",
                "email@email.com", true,
                "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.get(anyInt())).thenReturn(user);

        final Response post = resources.target("/api/1/users/1").request().get();
        final UserModel userModel = post.readEntity(UserModel.class);

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus());
        assertEquals(user.getUsername(), userModel.getUsername());
        assertEquals(user.getFirstName(), userModel.getFirstName());
        assertEquals(user.getLastName(), userModel.getLastName());
        assertEquals(user.getEmail(), userModel.getEmail());
        assertEquals(user.getEnabled(), userModel.getEnabled());
        assertEquals(user.getRoles().size(), userModel.getRoles().size());
        assertEquals(user.getRoles().get(0), userModel.getRoles().get(0));
        assertEquals(user.getRoles().get(1), userModel.getRoles().get(1));
        assertEquals(user.getRoles().get(2), userModel.getRoles().get(2));
        assertEquals(user.getRoles().get(3), userModel.getRoles().get(3));
    }

    @Test
    void testGetInvalidEmail() {
        final DatabaseUser user = new DatabaseUser(1, "username", "first", "last",
            "email.email.com", true,
            "password!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.get(anyInt())).thenReturn(user);

        final Response post = resources.target("/api/1/users/1").request()
            .get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), post.getStatus());
    }

    @Test
    void testAddUser() {
        final User user = new User( "username", "first", "last",
                "email@email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.find(anyString())).thenReturn(null);
        when(roleDAO.findById(anyInt())).thenReturn(new DatabaseUserRole(1, "any", "any"));
        doNothing().when(userDAO).insert(any());

        final Response post = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(Response.Status.CREATED.getStatusCode(), post.getStatus());
    }

    @Test
    void testUpdateUser() {
        final User user = new User( "username", "first", "last",
                "email@email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.find(anyString())).thenReturn(null);
        when(roleDAO.findById(anyInt())).thenReturn(new DatabaseUserRole(1, "any", "any"));
        doNothing().when(userDAO).insert(any());
        doNothing().when(userDAO).update(any());

        resources.target("/api/1/users").request()
                .post(Entity.json(user));

        when(userDAO.get(1)).thenReturn(new DatabaseUser(1, "username", "first", "last",
                "email@email.com", true, "Pa$$w0rd!", Arrays.asList(1,2,3,4)));

        final User update = new User( "username", "second", "third",
                "email@email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
                .put(Entity.json(update));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), post.getStatus());
    }

    @Test
    void testBadEmail() {
        final User user = new User( "username", "first", "last",
                "email.email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.find(anyString())).thenReturn(null);
        when(roleDAO.findById(anyInt())).thenReturn(new DatabaseUserRole(1, "any", "any"));
        doNothing().when(userDAO).insert(any());

        final Response post = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(422, post.getStatus());
    }

    @Test
    void testBadPassword() {
        final User user = new User( "username", "first", "last",
                "email@email.com", true,
                "password", Arrays.asList(1, 2, 3, 4));

        when(userDAO.find(anyString())).thenReturn(null);
        when(roleDAO.findById(anyInt())).thenReturn(new DatabaseUserRole(1, "any", "any"));
        doNothing().when(userDAO).insert(any());

        final Response post = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(422, post.getStatus());
    }



}