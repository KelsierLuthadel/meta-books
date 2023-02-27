package net.kelsier.bookshelf.framework.db;

import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.MetaBooks;
import net.kelsier.bookshelf.api.model.UserModel;
import net.kelsier.bookshelf.api.resource.UserAdministration;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import net.kelsier.bookshelf.framework.validator.CheckedValidationException;
import net.kelsier.bookshelf.framework.validator.ObjectValidator;
import net.kelsier.bookshelf.framework.validator.ValidationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class UserTest {
    @Mock static final UserDAO userDAO = mock(UserDAO.class);
    @Mock static final RoleDAO roleDAO = mock(RoleDAO.class);
    private static final ResourceExtension resources = ResourceExtension.builder()
        .addResource(new UserAdministration(userDAO, roleDAO))
        .build();


    @Test
    void testGetUsers() {
        final List<User> users = new ArrayList<>();

        users.add(new User(1, "username", "first", "last",
            "email@email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        when(userDAO.getAll()).thenReturn(users);

        final Response post = resources.target("/api/1/users").request()
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus());
    }

    @Test
    void testGetInvalidEmail() {
        final List<User> users = new ArrayList<>();

        users.add(new User(1, "username", "first", "last",
            "email.email.com", true,
            "Pa$$w0rd!", Arrays.asList(1, 2, 3, 4)));

        when(userDAO.getAll()).thenReturn(users);

        final Response post = resources.target("/api/1/users").request()
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), post.getStatus());

    }

//        final User user = new User(1,
//            "username", "firstName", "lastName",
//            "name.email.com", true, "PaSsw0rd!",
//            Arrays.asList(1,2,3));
//
//        try {
//            ObjectValidator.validateMapping(user);
//        } catch (ValidationException e) {
//            throw new RuntimeException(e);
//        }
//        final String email = user.getEmail();
//        int bar = 1;


}