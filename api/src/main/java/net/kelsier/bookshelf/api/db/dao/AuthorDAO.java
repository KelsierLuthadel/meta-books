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

import net.kelsier.bookshelf.api.db.mapper.AuthorMapper;
import net.kelsier.bookshelf.api.db.model.Author;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
/*
    public.authors (id SERIAL PRIMARY KEY, name TEXT NOT NULL , sort TEXT)
 */
/**
 * API to retrieve authors from the database.
 * Authors are stored in the following schema:
 * <p>
 * {@code public.authors (id SERIAL PRIMARY KEY, name TEXT NOT NULL , sort TEXT)}
 * </p>
 */

/**
 * DAO to map an author object in the database to a Java object so that it can be returned RESTfully.
 * Authors are stored in the following schema:
 * <p>
 * {@code public.authors (id SERIAL PRIMARY KEY, name TEXT NOT NULL , sort TEXT)}
 * </p>
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(AuthorMapper.class)
public interface AuthorDAO {

    /**
     * Get a single author from the database
     *
     * @param id - Author ID
     * @return - An object representing an author
     */
    @SqlQuery("SELECT * FROM authors WHERE ID = :id")
    Author get(@Bind("id") int id);

    /**
     * Get all authors from the database using pagination and sorting.
     *
     * @param limit - Total number of authors to return
     * @param offset - Starting position
     * @param order - Tolumn used for ordering
     * @param direction - sort direction applies
     * @return - A list of authors
     */
    @SqlQuery("SELECT * FROM authors ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Author> get(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Geta list of authors based on a search clause from the database using pagination and sorting.
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
     * @param limit - Total number of authors to return
     * @param offset - Starting position
     * @param order - Tolumn used for ordering
     * @param direction - sort direction applies
     * @return - A list of authors
     */
    @SqlQuery("SELECT * FROM authors WHERE <column> <clause> :text ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Author> find(
            @Bind("text") String text,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit, @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Insert a new author into the database
     * @param author - An object representing an author
     */
    @SqlUpdate("INSERT INTO authors (name, sort) " +
        "values (:name, :sort)")
    @GetGeneratedKeys
    void insert(@BindBean Author author);

    /**
     * Delete all authors from the database, this is used when re-creating the database contents.
     * Use with cuation.
     */
    @SqlUpdate("DELETE FROM authors")
    void purge();
}
