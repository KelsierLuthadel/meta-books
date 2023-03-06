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

package net.kelsier.bookshelf.api.db.mapper;


import net.kelsier.bookshelf.api.db.model.Book;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class BookMapper implements RowMapper<Book> {

    /**
     *
     *
     * @param resultSet results from a query
     * @param statementContext context
     * @return
     * @throws SQLException Thrown when there was a database error
     */
    public Book map(final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        return new Book(
                resultSet.getInt("ID"),
                resultSet.getString("TITLE"),
                resultSet.getString("SORT"),
                resultSet.getTimestamp("DATE_ADDED"),
                resultSet.getTimestamp("PUBLICATION_DATE"),
                resultSet.getDouble("SERIES_INDEX"),
                resultSet.getString("ISBN"),
                resultSet.getString("PATH"),
                resultSet.getBoolean("HAS_COVER"),
                resultSet.getTimestamp("LAST_MODIFIED"));
    }
}
