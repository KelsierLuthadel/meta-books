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

package net.kelsier.bookshelf.migrations.mapper;


import net.kelsier.bookshelf.migrations.model.CustomColumn;
import net.kelsier.bookshelf.migrations.model.CustomColumns;
import net.kelsier.bookshelf.migrations.model.Data;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
id  SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    format TEXT NOT NULL ,
    uncompressed_size INTEGER NOT NULL,
    name TEXT NOT NULL,
 */

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class CustomColumnsMapper implements RowMapper<CustomColumns> {

    /**
     *
     *
     * @param resultSet results from a query
     * @param statementContext context
     * @return
     * @throws SQLException Thrown when there was a database error
     */
    public CustomColumns map(final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        return new CustomColumns(
                resultSet.getInt("ID"),
                resultSet.getString("LABEL"),
                resultSet.getString("NAME"),
                resultSet.getString("DATATYPE"),
                resultSet.getString("DISPLAY"),
                resultSet.getBoolean("MULTIPLE"),
                resultSet.getBoolean("NORMALIZED"));
    }
}
