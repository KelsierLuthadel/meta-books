package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUser;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;
import net.kelsier.bookshelf.framework.encryption.PasswordEncrypt;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Authenticator
 */
public class BasicAuthenticator  implements Authenticator<BasicCredentials, UserAuth> {
    private final UserDAO userDAO;
    private final @Valid EncryptionConfiguration cipherConfiguration;

    public BasicAuthenticator(final UserDAO userDAO, final EncryptionConfiguration cipherConfiguration) {
        this.userDAO = userDAO;
        this.cipherConfiguration = cipherConfiguration;
    }
    @Override
    public Optional<UserAuth> authenticate(BasicCredentials credentials) {
        final DatabaseUser user = userDAO.find(credentials.getUsername());

        if (null != user && user.getEnabled() && new PasswordEncrypt(cipherConfiguration).checkPassword(credentials.getPassword(), user.getPassword())) {
            final UserAuth userAuth = new UserAuth(credentials.getUsername(), credentials.getPassword());
            userAuth.setId(user.getId());
            return Optional.of(userAuth);
        }

        return Optional.empty();
    }
}
