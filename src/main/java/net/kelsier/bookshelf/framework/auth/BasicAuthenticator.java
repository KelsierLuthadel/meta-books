package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import net.kelsier.bookshelf.framework.db.DatabaseUser;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Authenticator
 */
public class BasicAuthenticator  implements Authenticator<BasicCredentials, UserAuth> {
    private final UserDAO userDAO;

    public BasicAuthenticator(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public Optional<UserAuth> authenticate(BasicCredentials credentials) {
        final DatabaseUser user = userDAO.find(credentials.getUsername());

        if (null != user && user.getEnabled() && new PasswordEncrypt().checkPassword(credentials.getPassword(), user.getPassword())) {
            final UserAuth userAuth = new UserAuth(credentials.getUsername(), credentials.getPassword());
            userAuth.setId(user.getId());
            return Optional.of(userAuth);
        }

        return Optional.empty();
    }
}
