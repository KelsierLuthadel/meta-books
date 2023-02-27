/*
 * Copyright (c) 2023 Kelsier Luthadel
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
 */

package net.kelsier.bookshelf.framework.db.dao;

import net.kelsier.bookshelf.framework.db.User;
import net.kelsier.bookshelf.framework.db.map.UserMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for users in a database
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@RegisterRowMapper(UserMapper.class)
public interface UserDAO {

    /**
     *
     * @return A list of user roles
     */
    @SqlQuery("select * from USERS")
    List<User> getAll();

    @SqlQuery("select * from USERS WHERE ID = :id")
    User get(@Bind("id") Integer id);

    /**
     * Return a user using the id
     *
     * @param id the id of the users
     *
     * @return A user object
     */
    @SqlQuery("select * from USERS where ID = :id")
    User findById(@Bind("id") int id);

    @SqlQuery("select * from USERS where USERNAME = :username AND PASSWORD = :password")
    User find(@Bind("username") String username, @Bind("password") String password);

    @SqlQuery("select id from USERS where USERNAME = :username")
    User getUserId(@Bind("username") String username);

    /**
     * Delete a user
     *
     * @param id the id of the user
     */
    @SqlUpdate("delete from USERS where ID = :id")
    void deleteById(@Bind("id") int id);

    /**
     * Update user
     *
     * @param user the user to update
     */
    @SqlUpdate("update USERS set USERNAME = :username, FIRSTNAME = :firstName, LASTNAME = :lastName, EMAIL = :email, " +
            "ENABLED = :enabled, PASSWORD = :password, ROLES = :roles  where ID = :id")
    void update(@BindBean User user);

    /**
     * Add a new user
     *
     * @param user the user  to add
     */
    @SqlUpdate("insert into USERS (USERNAME, FIRSTNAME, LASTNAME, EMAIL, ENABLED, PASSWORD, ROLES) " +
            "values (:username, :firstName, :lastName, :email, :enabled, :password, :roles)")
    void insert(@BindBean User user);
}
