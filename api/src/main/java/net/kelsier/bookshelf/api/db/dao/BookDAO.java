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

import net.kelsier.bookshelf.api.db.mapper.BookMapper;
import net.kelsier.bookshelf.api.db.model.Book;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
/**
 * DAO to map a book object in the database to a Java object so that it can be returned RESTfully.
 * <p>Books are stored in the following schema:</p>
 * <style>table, th, td {border: 1px solid black;  border-collapse: collapse; padding: 5px 5px 5px 5px;} th {background-color:#DEDEDE}</style>
 * <table>
 *   <thead><tr><th>Name</th><th>Type</th><th>Description</th></tr></thead>
 *   <tbody>
 *     <tr><td>id</td><td>PRIMARY KEY</td><td>Book ID</td></tr>
 *     <tr><td>title</td><td>TEXT</td><td>Display name for the book</td></tr>
 *     <tr><td>sort</td><td>TEXT</td><td>Used for sorting books</td></tr>
 *     <tr><td>date_added</td><td>TIMESTAMP</td><td>Date that the book was added to the database</td></tr>
 *     <tr><td>publication_date</td><td>TIMESTAMP</td><td>Date that the book was published</td></tr>
 *     <tr><td>series_index</td><td>REAL</td><td>Book number if the book is part of a series</td></tr>
 *     <tr><td>isbn</td><td>TEXT</td><td>Book ISBN code</td></tr>
 *     <tr><td>path</td><td>TEXT</td><td>Relative path to book</td></tr>
 *     <tr><td>has_cover</td><td>BOOL</td><td>Flag to indicate if book has a cover</td></tr>
 *     <tr><td>last_modified</td><td>TIMESTAMP</td><td>Date that the book was last modified</td></tr>
 *   </tbody>
 * </table>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(BookMapper.class)
public interface BookDAO {
    /**
     * Get a single book from the database
     *
     * @param id - Book ID
     * @return - An object representing a book
     */
    @SqlQuery("SELECT * FROM books WHERE ID = :id")
    Book get(@Bind("id") int id);

    /**
     * Get all books from the database using pagination and sorting.
     *
     * @param limit - Total number of books to return
     * @param offset - Starting position
     * @param order - Tolumn used for ordering
     * @param direction - sort direction applies
     * @return - A list of books
     */
    @SqlQuery("SELECT * FROM books ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Book> get(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );


    /**
     * Geta list of books based on a search clause from the database using pagination and sorting.
     * The search clause is represented by column and operator, where the operator is one of:
     *
     * <ul>
     *     <li>=</li>
     *     <li>!=</li>
     *     <li>&lt;</li>
     *     <li>&lt;=</li>
     *     <li>&gt;</li>
     *     <li>&gt;=</li>
     *     <li>ILIKE</li>
     *     <li>NOT ILIKE</li>
     * </ul>
     *
     * @param text - The text used for searching
     * @param column - The column used for searching
     * @param clause - The search clause
     * @param limit - Total number of books to return
     * @param offset - Starting position
     * @param order - Tolumn used for ordering
     * @param direction - sort direction applies
     * @return - A list of books
     */
    @SqlQuery("SELECT * FROM books WHERE <column> <clause> :text  ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Book> find(
            @Bind("text") String text,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit, @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Geta list of books based on a search clause from the database using pagination and sorting.
     * The search clause is represented by column and operator, where the operator is one of:
     *
     * <ul>
     *     <li>=</li>
     *     <li>!=</li>
     *     <li>&lt;</li>
     *     <li>&lt;=</li>
     *     <li>&gt;</li>
     *     <li>&gt;=</li>
     *     <li>ILIKE</li>
     *     <li>NOT ILIKE</li>
     * </ul>
     *
     * @param value - The integer used for searching
     * @param column - The column used for searching
     * @param clause - The search clause
     * @param limit - Total number of books to return
     * @param offset - Starting position
     * @param order - Tolumn used for ordering
     * @param direction - sort direction applies
     * @return - A list of books
     */
    @SqlQuery("SELECT * FROM books WHERE <column> <clause> :value  ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Book> find(
            @Bind("value") Boolean value,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit, @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Insert a new book into the database
     * @param book - An object representing a book
     */
    @SqlUpdate("INSERT INTO books (title, sort, date_added, publication_date, series_index, isbn, path, has_cover, last_modified) " +
            "values (:title, :sort, :dateAdded, :publicationDate, :seriesIndex, :isbn, :path, :hasCover, :lastModified)")
    @GetGeneratedKeys
    void insert(@BindBean Book book);

    @SqlUpdate("DELETE FROM books")
    void purge();
}
