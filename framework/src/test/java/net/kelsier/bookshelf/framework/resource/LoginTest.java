/*
 * Copyright (c) Kelsier Luthadel 2023.
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
 *
 */

package net.kelsier.bookshelf.framework.resource;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUser;
import net.kelsier.bookshelf.framework.db.model.users.LoginModel;
import net.kelsier.bookshelf.framework.db.model.users.UserResponse;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class LoginTest {
    @Mock
    private static final UserDAO userDAO = mock(UserDAO.class);

    private DatabaseUser getUser(final String password, final boolean enabled) {
        final EncryptionConfiguration encryptionConfiguration = new EncryptionConfiguration(
            "SHA-256",
            16,
            50_000,
            "key"
        );

        final String encrypted = new PasswordEncrypt(encryptionConfiguration).encryptPassword(password);

        return new DatabaseUser(
            1,
            "name",
            "first name",
            "last name",
            "name@email.com",
            enabled,
            encrypted,
            Arrays.asList(1, 2, 3));
    }

    private static final ResourceExtension resources = ResourceExtension.builder()
        .addResource(new Login(userDAO, new EncryptionConfiguration(
            "SHA-256",
            16,
            50_000,
            "key"
        )))
        .build();

    @Test
    void testValidLogin() {
        final String password = "Pa££w0rd!";

        DatabaseUser user = getUser(password, true);
        when(userDAO.find(anyString())).thenReturn(user);

        final LoginModel login = new LoginModel("user", password);
        final UserResponse response;
        try (Response post = resources.target("/api/1/login").request().post(Entity.json(login))) {

            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(UserResponse.class);
        }
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getEmail(), response.getEmail());

        for (int role = 0; role < user.getRoles().size(); role++) {
            assertEquals(user.getRoles().get(role),response.getRoles().get(role),"Role should match");
        }
    }


    @Test
    void testInvalidLogin() {
        final String password = "U$er01bg.9!";

        DatabaseUser user = getUser(password, true);
        when(userDAO.find(anyString())).thenReturn(user);

        final LoginModel login = new LoginModel("user", "Pa££w0rd!");
        try (Response post = resources.target("/api/1/login").request().post(Entity.json(login))) {
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), post.getStatus(), "Status should be 401 unauthorised");
        }
    }

    @Test
    void testDisabledUser() {
        final String password = "Pa££w0rd!";

        DatabaseUser user = getUser(password, false);
        when(userDAO.find(anyString())).thenReturn(user);

        final LoginModel login = new LoginModel("user", password);
        try (Response post = resources.target("/api/1/login").request().post(Entity.json(login))) {
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), post.getStatus(), "Status should be 401 unauthorised");
        }
    }

    @Test
    void testNoUser() {
        when(userDAO.find(anyString())).thenReturn(null);

        final LoginModel login = new LoginModel("user", "Pa££word!");
        try (Response post = resources.target("/api/1/login").request().post(Entity.json(login))) {
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), post.getStatus(), "Status should be 401 unauthorised");
        }
    }

}