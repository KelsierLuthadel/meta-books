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
import net.kelsier.bookshelf.api.db.dao.BookDetailsDAO;
import net.kelsier.bookshelf.api.db.dao.CommentDAO;
import net.kelsier.bookshelf.api.db.dao.DataDAO;
import net.kelsier.bookshelf.api.db.dao.LanguageDAO;
import net.kelsier.bookshelf.api.db.dao.PublisherDAO;
import net.kelsier.bookshelf.api.db.dao.RatingDAO;
import net.kelsier.bookshelf.api.db.dao.SeriesDAO;
import net.kelsier.bookshelf.api.db.dao.TagDAO;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.db.tables.Table;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;
import net.kelsier.bookshelf.api.model.common.Pagination;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.util.Collections;
import java.util.List;

import static net.kelsier.bookshelf.api.db.types.DataTypes.*;

public class Connection {

    public static List<Entity> query(final Jdbi databaseConnection,
                                     final Table table,
                                     final @Valid ColumnLookup columnLookup,
                                     final @Valid Pagination pagination ) {
        if (null == columnLookup) {
            return performQuery(databaseConnection, table, pagination);
        } else {
            return performQuery(databaseConnection, table, columnLookup, pagination);
        }
    }

    public static Entity get(final Jdbi databaseConnection, final Table table, final Integer id) {
        switch (table) {
            case AUTHORS:
                return databaseConnection.onDemand(AuthorDAO.class).get(id);
            case BOOKS:
                return databaseConnection.onDemand(BookDAO.class).get(id);
            case COMMENTS:
                return databaseConnection.onDemand(CommentDAO.class).get(id);
            case DATA:
                return databaseConnection.onDemand(DataDAO.class).get(id);
            case LANGUAGES:
                return databaseConnection.onDemand(LanguageDAO.class).get(id);
            case PUBLISHERS:
                return databaseConnection.onDemand(PublisherDAO.class).get(id);
            case RATINGS:
                return databaseConnection.onDemand(RatingDAO.class).get(id);
            case SERIES:
                return databaseConnection.onDemand(SeriesDAO.class).get(id);
            case TAGS:
                return databaseConnection.onDemand(TagDAO.class).get(id);
            case BOOK_DETAILS:
                return databaseConnection.onDemand(BookDetailsDAO.class).get(id);
            default:
                throw new BadRequestException("Invalid query");
        }
    }

    private static List<Entity> performQuery(final Jdbi databaseConnection, final Table table, final @Valid Pagination pagination) {
        final int limit = pagination.getLimit();
        final int start = pagination.getStart();
        final String field = pagination.getSort().getField();
        final String direction = pagination.getSort().getDirection();

        switch (table) {
            case AUTHORS:
                return Collections.unmodifiableList(databaseConnection.onDemand(AuthorDAO.class).find(limit, start, field, direction));
            case BOOKS:
                return Collections.unmodifiableList(databaseConnection.onDemand(BookDAO.class).find(limit, start, field, direction));
            case COMMENTS:
                return Collections.unmodifiableList(databaseConnection.onDemand(CommentDAO.class).find(limit, start, field, direction));
            case DATA:
                return Collections.unmodifiableList(databaseConnection.onDemand(DataDAO.class).find(limit, start, field, direction));
            case LANGUAGES:
                return Collections.unmodifiableList(databaseConnection.onDemand(LanguageDAO.class).find(limit, start, field, direction));
            case PUBLISHERS:
                return Collections.unmodifiableList(databaseConnection.onDemand(PublisherDAO.class).find(limit, start, field, direction));
            case RATINGS:
                return Collections.unmodifiableList(databaseConnection.onDemand(RatingDAO.class).find(limit, start, field, direction));
            case SERIES:
                return Collections.unmodifiableList(databaseConnection.onDemand(SeriesDAO.class).find(limit, start, field, direction));
            case TAGS:
                return Collections.unmodifiableList(databaseConnection.onDemand(TagDAO.class).find(limit, start, field, direction));
            default:
                throw new BadRequestException("Invalid query");
        }
    }

    private static List<Entity> performQuery(final Jdbi databaseConnection,
                                     final Table table,
                                     final @Valid ColumnLookup columnLookup,
                                     final @Valid Pagination pagination ) {
        final String query = columnLookup.getField();
        final String value = columnLookup.getLookupValue();
        final String operator  = columnLookup.getOperator().getLabel();
        final int limit = pagination.getLimit();
        final int start = pagination.getStart();
        final String field = pagination.getSort().getField();
        final String direction = pagination.getSort().getDirection();

        switch (table) {
            case AUTHORS:
                return Collections.unmodifiableList(databaseConnection.onDemand(AuthorDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            case BOOKS:
                if (BOOL == columnLookup.getDataType()) {
                    return Collections.unmodifiableList(databaseConnection.onDemand(BookDAO.class).find(Boolean.parseBoolean(value), query,
                        operator,limit, start, field, direction));
                } else {
                    return Collections.unmodifiableList(databaseConnection.onDemand(BookDAO.class).find(value, query, operator, limit, start, field, direction));
                }
            case COMMENTS:
                return Collections.unmodifiableList(databaseConnection.onDemand(CommentDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            case DATA:
                if (INT == columnLookup.getDataType()) {
                    return Collections.unmodifiableList(databaseConnection.onDemand(DataDAO.class).find(Integer.parseInt(value), query,
                        operator, limit, start, field, direction));
                } else {
                    return Collections.unmodifiableList(databaseConnection.onDemand(DataDAO.class).find(value, query, operator, limit, start, field, direction));
                }
            case LANGUAGES:
                return Collections.unmodifiableList(databaseConnection.onDemand(LanguageDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            case PUBLISHERS:
                return Collections.unmodifiableList(databaseConnection.onDemand(PublisherDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            case RATINGS:
                return Collections.unmodifiableList(databaseConnection.onDemand(RatingDAO.class).find(Double.parseDouble(value),query, operator,
                    limit, start, field, direction));
            case SERIES:
                return Collections.unmodifiableList(databaseConnection.onDemand(SeriesDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            case TAGS:
                return Collections.unmodifiableList(databaseConnection.onDemand(TagDAO.class).find(value,query, operator,
                    limit, start, field, direction));
            default:
                throw new BadRequestException("Invalid query");
        }
    }


}
