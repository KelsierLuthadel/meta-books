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

package net.kelsier.bookshelf.framework.db.mapper.users;


import net.kelsier.bookshelf.framework.db.model.users.DatabaseUserRole;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map a result set to a role object
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public class RoleMapper implements RowMapper<DatabaseUserRole> {

    /**
     * Map a result set to a role object
     *
     * @param resultSet results from a query
     * @param statementContext context
     * @return A user role
     * @throws SQLException Thrown when there was a database error
     */
    public DatabaseUserRole map(final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        return new DatabaseUserRole(
                resultSet.getInt("ID"),
                resultSet.getString("ROLE"),
                resultSet.getString("DESCRIPTION"));
    }
}
