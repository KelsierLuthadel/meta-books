package net.kelsier.bookshelf.framework.auth;

import io.dropwizard.auth.Authorizer;
import net.kelsier.bookshelf.framework.db.User;
import net.kelsier.bookshelf.framework.db.UserRole;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;

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
        final User authUser = userDAO.find(user.getUsername(),user.getPassword());

        if (null == authUser) {
            return false;
        }

        final List<String> userRoles = new ArrayList<>();

        authUser.getRoles().forEach((Integer roleId) -> {
            final UserRole userRole = roleDAO.findById(roleId);
            if (null != userRole) {
                userRoles.add(userRole.getRole());
            }
        });

        return userRoles.contains(role);
    }
}
