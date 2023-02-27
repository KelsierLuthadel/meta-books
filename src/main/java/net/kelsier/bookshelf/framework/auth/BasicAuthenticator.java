package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import net.kelsier.bookshelf.framework.db.User;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;

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
        final User user = userDAO.find(credentials.getUsername(), credentials.getPassword());

        if (null == user) {
            return Optional.empty();
        }

        final UserAuth userAuth = new UserAuth(credentials.getUsername(),credentials.getPassword());
        userAuth.setId(user.getId());
        return Optional.of(userAuth);
    }
}
