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

package net.kelsier.bookshelf.framework.db.dao.bookshelf;

import net.kelsier.bookshelf.framework.db.mapper.bookshelf.BookMapper;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
/*
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL DEFAULT 'Unknown',
    sort TEXT ,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publication_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    series_index REAL NOT NULL DEFAULT 1.0,
    isbn TEXT DEFAULT '' ,
    path TEXT NOT NULL DEFAULT '',
    has_cover BOOL DEFAULT false,
    last_modified TIMESTAMP NOT NULL DEFAULT '2000-01-01 00:00:00+00:00'
);
 */
/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(BookMapper.class)
public interface BookDAO {
    @SqlQuery("SELECT * FROM books WHERE ID = :id")
    Book get(@Bind("id") int id);

    @SqlQuery("SELECT * FROM books LIMIT 10")
    List<Book> get();

    @SqlUpdate("INSERT INTO books (title, sort, date_added, publication_date, series_index, isbn, path, has_cover, last_modified) " +
            "values (:title, :sort, :dateAdded, :publicationDate, :seriesIndex, :isbn, :path, :hasCover, :lastModified)")
    @GetGeneratedKeys
    long insert(@BindBean Book book);

    @SqlUpdate("DELETE FROM books")
    void purge();
}
