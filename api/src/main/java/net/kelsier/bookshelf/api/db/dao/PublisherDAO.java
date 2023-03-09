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

import net.kelsier.bookshelf.api.db.mapper.PublisherMapper;
import net.kelsier.bookshelf.api.db.model.Publisher;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO to map a publisher object in the database to a Java object so that it can be returned RESTfully.
 * <p>Publishers are stored in the following schema:</p>
 * <style>table, th, td {border: 1px solid black;  border-collapse: collapse; padding: 5px 5px 5px 5px;} th {background-color:#DEDEDE}</style>
 * <table>
 *   <thead><tr><th>Name</th><th>Type</th><th>Description</th></tr></thead>
 *   <tbody>
 *     <tr><td>id</td><td>PRIMARY KEY</td><td>Publisher ID</td></tr>
 *     <tr><td>name</td><td>TEXT</td><td>Publisher display name</td></tr>
 *     <tr><td>sort</td><td>TEXT</td><td>Publisher name used for sorting</td></tr>
 *   </tbody>
 * </table>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(PublisherMapper.class)
public interface PublisherDAO {
    /**
     * Get a single publisher from the database
     *
     * @param id Publisher ID
     * @return An object representing a publisher
     */
    @SqlQuery("SELECT * FROM publishers WHERE ID = :id")
    Publisher get(@Bind("id") int id);

    /**
     * Get all publishers from the database using pagination and sorting.
     *
     * @param limit Total number of publishers to return
     * @param offset Starting position
     * @param order Tolumn used for ordering
     * @param direction sort direction applies
     * @return A list of publishers
     */
    @SqlQuery("SELECT * FROM publishers ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Publisher> find(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Geta list of publishers based on a search clause from the database using pagination and sorting.
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
     * @param text The value used for searching
     * @param column The column used for searching
     * @param clause The search clause
     * @param limit Total number of publishers to return
     * @param offset Starting position
     * @param order Tolumn used for ordering
     * @param direction sort direction applies
     * @return A list of publishers
     */
    @SqlQuery("SELECT * FROM publishers WHERE <column> <clause> :text ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Publisher> find(
            @Bind("text") String text,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    /**
     * Insert a new publisher into the database
     * @param publisher An object representing a publisher
     * @return row id for the newly created data
     */
    @SqlUpdate("INSERT INTO publishers (id, name, sort) " +
        "values (:id, :name, :sort)")
    @GetGeneratedKeys
    long insert(@BindBean Publisher publisher);


    /**
     * Delete all publishers from the database, this is used when re-creating the database contents.
     * Use with caution.
     */
    @SqlUpdate("DELETE FROM publishers")
    void purge();
}
