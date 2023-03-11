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
import net.kelsier.bookshelf.api.db.mapper.BookListMapper;
import net.kelsier.bookshelf.api.db.model.view.BookDetails;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

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
@RegisterRowMapper(BookListMapper.class)
public interface BookListDAO {
    /**
     * Get a list of books linked to an author from the database using pagination and sorting.
     *
     * @param authorId The author id linked with a book
     * @param limit Total number of books to return
     * @param offset Starting position
     * @param order Column used for ordering
     * @param direction sort direction applies
     * @return A list of books
     */
    @SqlQuery(
            "select " +
                    "book.id," +
                    "book.title," +
                    "author.author," +
                    "series.series," +
                    "book.series_index," +
                    "language.language ," +
                    "data.format," +
                    "data.size," +
                    "book.has_cover," +
                    "book.publication_date," +
                    "book.path," +
                    "tag.tags " +
                    "FROM books book " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, authors.name  as author, authors.id as author_id " +
                    "FROM books ref " +
                    "INNER JOIN books_authors_link AS link ON link.book=ref.id " +
                    "INNER JOIN authors AS authors ON authors.id=link.author " +
                    "GROUP BY ref.id, authors.name, authors.id " +
                    ") author USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, series.name as series " +
                    "FROM books ref " +
                    "INNER JOIN books_series_link AS link ON link.book=ref.id " +
                    "INNER JOIN series AS series ON series.id=link.series " +
                    "GROUP BY ref.id, series.name " +
                    ") series USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, publishers.name as publisher " +
                    "FROM books ref " +
                    "INNER JOIN books_publishers_link AS link ON link.book=ref.id " +
                    "INNER JOIN publishers AS publishers ON publishers.id=link.publisher " +
                    "GROUP BY ref.id, publishers.name " +
                    ") publisher USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, languages.lang_code as language " +
                    "FROM books ref " +
                    "INNER JOIN books_languages_link AS link ON link.book=ref.id " +
                    "INNER JOIN languages AS languages ON languages.id=link.lang_code " +
                    "GROUP BY ref.id, languages.lang_code " +
                    ") language USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, data.format as format, data.uncompressed_size AS size " +
                    "FROM books ref " +
                    "INNER JOIN data AS data ON data.id=ref.id " +
                    ") data USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, comments.text as comments " +
                    "FROM books ref " +
                    "INNER JOIN comments AS comments ON comments.book=ref.id " +
                    ") comments USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, array_agg(tags.name) AS tags " +
                    "FROM books ref " +
                    "INNER JOIN books_tags_link AS link ON link.book=ref.id " +
                    "INNER JOIN tags AS tags ON tags.id=link.tag " +
                    "GROUP BY ref.id " +
                    ") tag USING(id) " +

                    "LEFT JOIN ( " +
                    "SELECT ref.id AS id, array_agg(ident.type) AS identifier_type, array_agg(ident.val) as identifier_value " +
                    "FROM books ref " +
                    "JOIN identifiers AS ident on ident.book=ref.id " +
                    "GROUP BY ref.id " +
                    ") identifier USING(id) where author.author_id = :author"
    )
    List<BookDetails> findByAuthor(
            @Bind("author") int authorId,
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

}
