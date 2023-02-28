package net.kelsier.bookshelf.api.resource;

import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.User;
import net.kelsier.bookshelf.api.model.UserModel;
import net.kelsier.bookshelf.framework.db.DatabaseUser;
import net.kelsier.bookshelf.framework.db.DatabaseUserRole;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class UserAdministrationTest {
    @Mock static final UserDAO userDAO = mock(UserDAO.class);
    @Mock static final RoleDAO roleDAO = mock(RoleDAO.class);
    private static final ResourceExtension resources = ResourceExtension.builder()
        .addResource(new UserAdministration(userDAO, roleDAO))
        .build();

    @BeforeEach
    void setup() {
        when(userDAO.find(anyString())).thenReturn(null);
        when(roleDAO.findById(anyInt())).thenReturn(new DatabaseUserRole(1, "any", "any"));
        when(userDAO.insert(any())).thenReturn(1L);
        doNothing().when(userDAO).update(any());

    }

    // GET

    @Test
    void testGetUsers() {
        final List<DatabaseUser> users = new ArrayList<>();

        users.add(new DatabaseUser(1, "username", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        users.add(new DatabaseUser(2, "Another", "Name", "Surname",
            "noreply@email.com", false,
            "Pa$$w0rd!", Arrays.asList(5, 6)));

        when(userDAO.getAll()).thenReturn(users);

        final Response post = resources.target("/api/1/users").request().get();
        final List<UserModel> userModel = post.readEntity((new GenericType<>() {}));

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");
        assertEquals(users.get(0).getUsername(), userModel.get(0).getUsername(), "Username should match");
        assertEquals(users.get(0).getFirstName(), userModel.get(0).getFirstName(), "First name should match");
        assertEquals(users.get(0).getLastName(), userModel.get(0).getLastName(), "Last name should match");
        assertEquals(users.get(0).getEmail(), userModel.get(0).getEmail(), "Email should match");
        assertEquals(users.get(0).getEnabled(), userModel.get(0).getEnabled(), "Enabled should match");
        assertEquals(users.get(0).getRoles().size(), userModel.get(0).getRoles().size(), "Role size should match");
        assertEquals(users.get(0).getRoles().get(0), userModel.get(0).getRoles().get(0), "First role should match");
        assertEquals(users.get(0).getRoles().get(1), userModel.get(0).getRoles().get(1), "Second role should match");
        assertEquals(users.get(0).getRoles().get(2), userModel.get(0).getRoles().get(2), "Third role should match");
        assertEquals(users.get(0).getRoles().get(3), userModel.get(0).getRoles().get(3), "Fourth role should match");
    }

    @Test
    void testGetUser() {
        final DatabaseUser user = new DatabaseUser(1, "username", "first", "last",
                "email@email.com", true,
                "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.get(anyInt())).thenReturn(user);

        final Response post = resources.target("/api/1/users/1").request().get();
        final UserModel userModel = post.readEntity(UserModel.class);

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");
        assertEquals(user.getUsername(), userModel.getUsername(), "Username should match");
        assertEquals(user.getFirstName(), userModel.getFirstName(), "First name should match");
        assertEquals(user.getLastName(), userModel.getLastName(), "Last name should match");
        assertEquals(user.getEmail(), userModel.getEmail(), "Email should match");
        assertEquals(user.getEnabled(), userModel.getEnabled(), "Enabled should match");
        assertEquals(user.getRoles().size(), userModel.getRoles().size(), "Role size should match");
        assertEquals(user.getRoles().get(0), userModel.getRoles().get(0), "First role should match");
        assertEquals(user.getRoles().get(1), userModel.getRoles().get(1), "Second role should match");
        assertEquals(user.getRoles().get(2), userModel.getRoles().get(2), "Third role should match");
        assertEquals(user.getRoles().get(3), userModel.getRoles().get(3), "Fourth role should match");
    }

    // POST
    @Test
    void testAddUser() {
        final User user = new User("username", "first", "last",
                "email@email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response response = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),"Status should be 200 OK");

        final UserModel model = response.readEntity(UserModel.class);
        assertEquals(user.getUsername(), model.getUsername(), "Username should match");
        assertEquals(user.getFirstName(), model.getFirstName(), "First name should match");
        assertEquals(user.getLastName(), model.getLastName(), "Last name should match");
        assertEquals(user.getEmail(), model.getEmail(), "Email should match");
        assertEquals(user.getEnabled(), model.getEnabled(), "Enabled should match");
        assertEquals(user.getRoles().size(), model.getRoles().size(), "Number of roles should match");
        assertArrayEquals(user.getRoles().toArray(), model.getRoles().toArray(), "Roles should match");
    }

    @Test
    void testAddUserWithNoNames() {
        final User user = new User( "username", null, null,
            null, true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response response = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),"Status should be 200 OK");

        final UserModel model = response.readEntity(UserModel.class);
        assertEquals(user.getUsername(), model.getUsername(), "Username should match");
        assertEquals(user.getFirstName(), model.getFirstName(), "First name should match");
        assertEquals(user.getLastName(), model.getLastName(), "Last name should match");
        assertEquals(user.getEmail(), model.getEmail(), "Email should match");
        assertEquals(user.getEnabled(), model.getEnabled(), "Enabled should match");
        assertEquals(user.getRoles().size(), model.getRoles().size(), "Number of roles should match");
        assertArrayEquals(user.getRoles().toArray(), model.getRoles().toArray(), "Roles should match");
    }

    @Test
    void testAddUserWithEmptyNames() {
        final User user = new User( "username", "", "",
            "", true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response response = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),"Status should be 200 OK");

        final UserModel model = response.readEntity(UserModel.class);
        assertEquals(user.getUsername(), model.getUsername(), "Username should match");
        assertEquals(null, model.getFirstName(), "First name should match");
        assertEquals(null, model.getLastName(), "Last name should match");
        assertEquals(null, model.getEmail(), "Email should match");
        assertEquals(user.getEnabled(), model.getEnabled(), "Enabled should match");
        assertEquals(user.getRoles().size(), model.getRoles().size(), "Number of roles should match");
        assertArrayEquals(user.getRoles().toArray(), model.getRoles().toArray(), "Roles should match");

    }

    @Test
    void testUpdateUser() {
        when(userDAO.get(anyInt())).thenReturn(new DatabaseUser(
            1, "username", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        final User update = new User(
            "username", "second", "third",
                "email@email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
                .put(Entity.json(update));

        assertEquals(OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
        final UserModel model = post.readEntity(UserModel.class);

        assertEquals(update.getUsername(), model.getUsername(), "Username should match");
        assertEquals(update.getFirstName(), model.getFirstName(), "First name should match");
        assertEquals(update.getLastName(), model.getLastName(), "Last name should match");
        assertEquals(update.getEmail(), model.getEmail(), "Email should match");
        assertEquals(update.getEnabled(), model.getEnabled(), "Enabled should match");
        assertEquals(update.getRoles(), model.getRoles(), "Roles should match");
    }

    @Test
    void testDeleteUser() {
        when(userDAO.get(anyInt())).thenReturn(new DatabaseUser(
            1, "username", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        final Response post = resources.target("/api/1/users/1").request()
            .delete();

        assertEquals(OK.getStatusCode(), post.getStatus(),"Status should be 200 OK");
    }

    /**
     * Test for failures
     */

    @Test
    void testGetUser404() {
        when(userDAO.get(5)).thenReturn(null);

        final Response post = resources.target("/api/1/users/5").request()
            .get();

        assertEquals(NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    @Test
    void testAddUserNullName() {
        final User user = new User("user", "first", "last",
            "email@email.com", true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));
        when(userDAO.find(anyString())).thenReturn(new DatabaseUser(1, "user",
            "name", "name", "user@email.com", true,
            "Pa$$sw0rd!", Arrays.asList(1,2)));

        final Response response = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus(),"Status should be 400 BAD REQUEST");
    }

    @Test
    void testAddUserRoleDoesntExist() {
        final User user = new User("user", "first", "last",
            "email@email.com", true,
            "Pa$$sw0rd!", Arrays.asList(0, 1));

        when(userDAO.find("user")).thenReturn(null);

        when(roleDAO.findById(0)).thenReturn(new DatabaseUserRole(1, "admin:c", "create"));
        when(roleDAO.findById(1)).thenReturn(null);


        final Response response = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus(),"Status should be 400 BAD REQUEST");
    }

    @Test
    void testUpdateMissingUser() {
        when(userDAO.get(5)).thenReturn(null);

        final User update = new User( "username", "second", "third",
            "email@email.com", true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/5").request()
            .put(Entity.json(update));

        assertEquals(NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    @Test
    void testUpdateExistingUser() {
        when(userDAO.get(5)).thenReturn(new DatabaseUser(
            1, "name","first", "last",
            "email@email.com", true, "Pa££w0rd!", Arrays.asList(1,2,3)));

        when(userDAO.getUserId("differentName")).thenReturn(new DatabaseUser(
            1, "differentName","first", "last",
            "email@email.com", true, "Pa££w0rd!", Arrays.asList(1,2,3)));

        final User update = new User( "differentName", "second", "third",
            "email@email.com", true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/5").request()
            .put(Entity.json(update));

        assertEquals(BAD_REQUEST.getStatusCode(), post.getStatus(),"Status should be 400 BAD REQUEST");
    }

    @Test
    void testDeleteUser404() {
        when(userDAO.get(anyInt())).thenReturn(null);

        final Response post = resources.target("/api/1/users/1").request()
            .delete();

        assertEquals(NOT_FOUND.getStatusCode(), post.getStatus(),"Status should be 404 NOT FOUND");
    }

    /**
     * Test for invalid data
     */

    // GET
    @Test
    void testGetInvalidEmail() {
        final DatabaseUser user = new DatabaseUser(1, "username", "first", "last",
            "email.email.com", true,
            "password!", Arrays.asList(1, 2, 3, 4));

        when(userDAO.get(anyInt())).thenReturn(user);

        final Response post = resources.target("/api/1/users/1").request()
            .get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), post.getStatus(),
            "Status should be 500 INTERNAL SERVER ERROR");
    }

    @Test
    void testGetUsersWithInvalidEmail() {
        final List<DatabaseUser> users = new ArrayList<>();

        users.add(new DatabaseUser(1, "user", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        users.add(new DatabaseUser(2, "Another", "Name", "Surname",
            "noreply:email.com", false,
            "Pa$$w0rd!", Arrays.asList(5, 6)));

        when(userDAO.getAll()).thenReturn(users);

        final Response post = resources.target("/api/1/users").request().get();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), post.getStatus(),
            "Status should be 500 INTERNAL SERVER ERROR");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);
        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("server response email invalid format", msg.getErrors().get(0),
            "There should be an Invalid email format error");
    }

    // POST
    @Test
    void testBadEmail() {
        final User user = new User( "username", "first", "last",
                "email.email.com", true,
                "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email invalid format", msg.getErrors().get(0),
            "There should be an Invalid email format error");
    }

    @Test
    void testBadPassword() {
        final User user = new User( "username", "first", "last",
                "email@email.com", true,
                "password", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
                .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("password does not meet minimum requirements", msg.getErrors().get(0),
            "There should be an invalid password error");
    }

    @Test
    void testNullUsername() {
        final User user = new User( null, "first", "last",
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("username must not be null", msg.getErrors().get(0),
            "There should be an invalid password error");
    }

    @Test
    void testMinSizeUsername() {
        final User user = new User("ab", "first", "last",
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("username size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testMaxSizeUsername() {
        final String thirtyOneCharacters = new String(new char[31]).replace('\0', 'a');
        final User user = new User( thirtyOneCharacters, "first", "last",
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("username size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testMaxSizeNames() {
        final String thirtyOneCharacters = new String(new char[31]).replace('\0', 'a');
        final User user = new User(thirtyOneCharacters, thirtyOneCharacters, thirtyOneCharacters,
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(3, msg.getErrors().size(), "There should only be one validation error");
        msg.getErrors().forEach(err -> assertTrue(err.contains("size must be between 3 and 30"),
            "There should be a size limit error"));
    }

    @Test
    void testMinSizeEmail() {
        final User user = new User("user", "first", "last",
            "e@ba", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email size must be between 5 and 320", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testMaxSizeEmail() {
        final String largeEmailAddress = new String(new char[315]).replace('\0', 'a');
        final User user = new User("user", "first", "last",
            MessageFormat.format("a@{0}.com", largeEmailAddress), true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email size must be between 5 and 320", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testMaxSizePassword() {
        final String largePassword = new String(new char[28]).replace('\0', 'a');
        final User user = new User("user", "first", "last",
            "user@email.com", true,
            MessageFormat.format("A9£{0}", largePassword), Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users").request()
            .post(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("password does not meet minimum requirements", msg.getErrors().get(0),
            "There should be a password error");
    }

    // PUT
    @Test
    void testUpdateBadEmail() {
        final User user = new User( "username", "first", "last",
            "email.email.com", true,
            "Pa$$sw0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email invalid format", msg.getErrors().get(0),
            "There should be an Invalid email format error");
    }

    @Test
    void testUpdateBadPassword() {
        final User user = new User( "username", "first", "last",
            "email@email.com", true,
            "password", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("password does not meet minimum requirements", msg.getErrors().get(0),
            "There should be an invalid password error");
    }



    @Test
    void testUpdateMinSizeUsername() {
        final User user = new User("ab", "first", "last",
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("username size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxSizeUsername() {
        final String thirtyOneCharacters = new String(new char[31]).replace('\0', 'a');
        final User user = new User( thirtyOneCharacters, "first", "last",
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("username size must be between 3 and 30", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxSizeNames() {
        final String thirtyOneCharacters = new String(new char[31]).replace('\0', 'a');
        final User user = new User(thirtyOneCharacters, thirtyOneCharacters, thirtyOneCharacters,
            "email@email.com", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(3, msg.getErrors().size(), "There should only be one validation error");
        msg.getErrors().forEach(err -> assertTrue(err.contains("size must be between 3 and 30"),
            "There should be a size limit error"));
    }

    @Test
    void testUpdateMinSizeEmail() {
        final User user = new User("user", "first", "last",
            "e@ba", true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email size must be between 5 and 320", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxSizeEmail() {
        final String largeEmailAddress = new String(new char[315]).replace('\0', 'a');
        final User user = new User("user", "first", "last",
            MessageFormat.format("a@{0}.com", largeEmailAddress), true,
            "Pa££w0rd!", Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("email size must be between 5 and 320", msg.getErrors().get(0),
            "There should be an invalid size error");
    }

    @Test
    void testUpdateMaxSizePassword() {
        final String largePassword = new String(new char[28]).replace('\0', 'a');
        final User user = new User("user", "first", "last",
            "user@email.com", true,
            MessageFormat.format("A9£{0}", largePassword), Arrays.asList(1, 2, 3, 4));

        final Response post = resources.target("/api/1/users/1").request()
            .put(Entity.json(user));

        assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be UNPROCESSABLE_ENTITY");

        final ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);

        assertEquals(1, msg.getErrors().size(), "There should only be one validation error");
        assertEquals("password does not meet minimum requirements", msg.getErrors().get(0),
            "There should be a password error");
    }

}