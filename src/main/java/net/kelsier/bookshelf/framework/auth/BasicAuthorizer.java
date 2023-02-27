package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authorizer;

public class BasicAuthorizer implements Authorizer<UserAuth> {
    @Override
    public boolean authorize(UserAuth user, String role) {
        return user.getName().equals("good-guy") && role.equals("ADMIN");
    }
}
