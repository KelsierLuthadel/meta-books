/*
 * Copyright (c) Kelsier Luthadel 2023.
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
 *
 */

package net.kelsier.bookshelf.api.db.connection;

import net.kelsier.bookshelf.api.db.dao.AuthorDAO;
import net.kelsier.bookshelf.api.db.dao.BookDAO;
import net.kelsier.bookshelf.api.db.model.Author;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.db.tables.Table;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

public class Connection {

    public static List<Entity> query(final Jdbi databaseConnection, final Table table, final @Valid Pagination pagination) {
        final int limit = pagination.getLimit();
        final int start = pagination.getStart();
        final String field = pagination.getSort().getField();
        final String direction = pagination.getSort().getDirection();

        switch (table) {
            case AUTHORS:
                return Collections.unmodifiableList(databaseConnection.onDemand(AuthorDAO.class).get(limit, start, field, direction));
            case BOOKS:
                return Collections.unmodifiableList(databaseConnection.onDemand(BookDAO.class).get(limit, start, field, direction));
            default:
                throw new RuntimeException("");
        }
    }

    public static List<Entity> query(final Jdbi databaseConnection,
                                     final Table table,
                                     final @Valid ColumnLookup columnLookup,
                                     final @Valid Pagination pagination ) {
        final String query = columnLookup.getField();
        final String value = columnLookup.getValue();
        final String operator  = columnLookup.getOperator().getLabel();
        final int limit = pagination.getLimit();
        final int start = pagination.getStart();
        final String field = pagination.getSort().getField();
        final String direction = pagination.getSort().getDirection();

        switch (table) {
            case AUTHORS:
                return Collections.unmodifiableList(databaseConnection.onDemand(AuthorDAO.class).find(query, value, operator,
                    limit, start, field, direction));
            case BOOKS:
                return Collections.unmodifiableList(databaseConnection.onDemand(BookDAO.class).find(query, value, operator,
                    limit, start, field, direction));
            default:
                throw new RuntimeException("");
        }
    }

    public static Entity get(final Jdbi databaseConnection, final Table table, final Integer id) {
        switch (table) {
            case AUTHORS:
                return databaseConnection.onDemand(AuthorDAO.class).get(id);
            case BOOKS:
                return databaseConnection.onDemand(BookDAO.class).get(id);
            default:
                throw new RuntimeException("");
        }
    }
}
