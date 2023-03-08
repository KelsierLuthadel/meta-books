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

package net.kelsier.bookshelf.api.db.dao;

import net.kelsier.bookshelf.api.db.mapper.BookPublisherLinkMapper;
import net.kelsier.bookshelf.api.db.model.BookPublisherLink;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/*
 * CREATE TABLE books_publishers_link (
 *     id INTEGER PRIMARY KEY,
 *     book INTEGER NOT NULL,
 *     publisher INTEGER NOT NULL,
 *     UNIQUE(book)
 * )
 */

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(BookPublisherLinkMapper.class)
public interface BookPublisherLinkDAO {
    @SqlQuery("SELECT * FROM books_publishers_link WHERE ID = :id")
    BookPublisherLink get(@Bind("id") int id);

    @SqlUpdate("INSERT INTO books_publishers_link (id, book, publisher) values (:id, :book, :publisher)")
    @GetGeneratedKeys
    long insert(@BindBean BookPublisherLink bookPublisherLink);

    /**
     * Delete all books-publishers links from the database, this is used when re-creating the database contents.
     * Use with caution.
     */
    @SqlUpdate("DELETE FROM books_publishers_link")
    void purge();
}
