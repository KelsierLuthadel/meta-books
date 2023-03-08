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

import net.kelsier.bookshelf.api.db.mapper.BookDetailsMapper;
import net.kelsier.bookshelf.api.db.model.BookDetails;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterRowMapper(BookDetailsMapper.class)
public interface BookDetailsDAO {
    @SqlQuery("select " +
        "books.id," +
        "books.title," +
        "authors.name AS author," +
        "series.name AS series," +
        "books.series_index," +
        "publishers.name AS publisher," +
        "books.isbn," +
        "languages.lang_code AS language," +
        "data.format AS format," +
        "data.uncompressed_size AS size," +
        "books.has_cover," +
        "books.date_added," +
        "books.publication_date," +
        "books.last_modified," +
        "books.path," +
        "comments.text AS comments " +
        "FROM books INNER JOIN books_authors_link ON books_authors_link.book=books.id " +
        "INNER JOIN authors ON authors.id=books_authors_link.author " +
        "INNER JOIN books_languages_link ON books_languages_link.book=books.id " +
        "INNER JOIN languages ON languages.id=books_languages_link.lang_code " +
        "INNER JOIN books_publishers_link ON books_publishers_link.book=books.id " +
        "INNER JOIN publishers ON publishers.id=books_publishers_link.publisher " +
        "INNER JOIN books_series_link ON books_series_link.book=books.id " +
        "INNER JOIN series ON series.id=books_series_link.series " +
        "INNER JOIN data ON data.id=books.id " +
        "INNER JOIN comments ON comments.book=books.id " +
        "WHERE books.id = :id")
    BookDetails get(@Bind("id") int id);

}
