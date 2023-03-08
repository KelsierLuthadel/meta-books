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


import net.kelsier.bookshelf.api.db.model.BookDetails;
import net.kelsier.bookshelf.api.db.model.Tag;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map a database query to am {@link BookDetails} class
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public class BookDetailsMapper implements RowMapper<BookDetails> {

    /**
     * Map a database query to am {@link BookDetails} class
     *
     * @param resultSet results from a query
     * @param statementContext context
     * @return {@link BookDetails} object representing the results of a query
     * @throws SQLException Thrown when there was a database error
     */
    public BookDetails map(final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        return new BookDetails(
                resultSet.getInt("ID"),
                resultSet.getString("TITLE"),
                resultSet.getString("AUTHOR"),
                resultSet.getString("SERIES"),
                resultSet.getInt("SERIES_INDEX"),
                resultSet.getString("PUBLISHER"),
                resultSet.getString("ISBN"),
                resultSet.getString("LANGUAGE"),
                resultSet.getString("FORMAT"),
                resultSet.getInt("SIZE"),
                resultSet.getBoolean("HAS_COVER"),
                resultSet.getTimestamp("DATE_ADDED"),
                resultSet.getTimestamp("PUBLICATION_DATE"),
                resultSet.getTimestamp("LAST_MODIFIED"),
                resultSet.getString("PATH"),
                resultSet.getString("COMMENTS")
        );
    }
}
