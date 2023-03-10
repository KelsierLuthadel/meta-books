package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authorizer;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUser;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUserRole;
import net.kelsier.bookshelf.framework.db.dao.users.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Authorizer
 *
 */
public class BasicAuthorizer implements Authorizer<UserAuth> {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public BasicAuthorizer(final UserDAO userDAO, final RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;

    }
    @Override
    public boolean authorize(@Valid final UserAuth user, final String role) {
        final DatabaseUser authUser = userDAO.find(user.getUsername());

        if (null == authUser) {
            return false;
        }

        final List<String> userRoles = new ArrayList<>();

        authUser.getRoles().forEach((Integer roleId) -> {
            final DatabaseUserRole userRole = roleDAO.findById(roleId);
            if (null != userRole) {
                userRoles.add(userRole.getRole());
            }
        });

        return userRoles.contains(role);
    }
}
