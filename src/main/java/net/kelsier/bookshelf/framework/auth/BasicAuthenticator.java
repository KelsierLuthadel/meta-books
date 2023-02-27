package net.kelsier.bookshelf.framework.auth;



import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class BasicAuthenticator  implements Authenticator<BasicCredentials, UserAuth> {
    @Override
    public Optional<UserAuth> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            return Optional.of(new UserAuth(credentials.getUsername(), credentials.getPassword()));
        }
        return Optional.empty();
    }
}