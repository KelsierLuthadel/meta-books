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

package net.kelsier.bookshelf.api.db.dao.view;

import net.kelsier.bookshelf.api.db.dao.AuthorDAO;
import net.kelsier.bookshelf.api.db.dao.BookDAO;
import net.kelsier.bookshelf.api.db.dao.metadata.*;
import net.kelsier.bookshelf.api.db.mapper.view.BookDetailsMapper;
import net.kelsier.bookshelf.api.db.model.view.BookMetadata;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

/**
 * DAO to map a book object in the database to a Java object so that it can be returned RESTfully.
 * Data is taken from the following tables:
 * <ul>
 *     <li>{@link BookDAO}</li>
 *     <li>{@link AuthorDAO}</li>
 *     <li>{@link LanguageDAO}</li>
 *     <li>{@link PublisherDAO}</li>
 *     <li>{@link SeriesDAO}</li>
 *     <li>{@link DataDAO}</li>
 *     <li>{@link CommentDAO}</li>
 * </ul>
 * </table>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(BookDetailsMapper.class)
public interface BookDetailsDAO {
    /**
     * Get a book and associated metadata
     *
     * @param id Book ID
     * @return An object representing a book
     */
    @SqlQuery("SELECT id, title, author_id, author, series, series_index, publisher, isbn, identifier_type, " +
            "identifier_value, language, format, size, has_cover, date_added, publication_date, last_modified, " +
            "path, comments, tags " +
            "FROM book_metadata WHERE id = :id")
    BookMetadata get(@Bind("id") int id);

}
