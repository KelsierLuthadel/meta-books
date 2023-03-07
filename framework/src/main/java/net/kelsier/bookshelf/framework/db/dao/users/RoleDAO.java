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

package net.kelsier.bookshelf.framework.db.dao.users;

import net.kelsier.bookshelf.framework.db.mapper.users.RoleMapper;
import net.kelsier.bookshelf.framework.db.model.users.DatabaseUserRole;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for user roles in a database
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(RoleMapper.class)
public interface RoleDAO {

    /**
     *
     * @return A list of user roles
     */
    @SqlQuery("select * from ROLES")
    List<DatabaseUserRole> getAll();

    /**
     * Return a user role using the id
     *
     * @param id the id of the user role
     *
     * @return A user role object
     */
    @SqlQuery("select * from ROLES where ID = :id")
    DatabaseUserRole findById(@Bind("id") Integer id);

    @SqlQuery("select * from ROLES where ROLE = :role")
    DatabaseUserRole findByName(@Bind("id") String role);

    /**
     * Delete a user role
     *
     * @param id the id of the user role
     */
    @SqlUpdate("delete from ROLES where ID = :id")
    void deleteById(@Bind("id") Integer id);

    /**
     * Update user roles
     *
     * @param userRole the user role to replace
     */
    @SqlUpdate("update ROLES set ROLE = :role, DESCRIPTION = :description where ID = :id")
    void update(@BindBean DatabaseUserRole userRole);

    /**
     * Add a new role
     *
     * @param user the user role to add
     */
    @SqlUpdate("insert into ROLES (ROLE, DESCRIPTION) values (:role, :description)")
    @GetGeneratedKeys
    long insert(@BindBean DatabaseUserRole user);
}
