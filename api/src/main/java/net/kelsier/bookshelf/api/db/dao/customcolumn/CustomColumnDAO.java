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

package net.kelsier.bookshelf.api.db.dao.customcolumn;

import net.kelsier.bookshelf.api.db.mapper.customcolumn.CustomColumnMapper;
import net.kelsier.bookshelf.api.db.model.customcolumn.CustomColumn;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/*
CREATE TABLE custom_column_(
                    id    SERIAL PRIMARY KEY,
                    value TEXT NOT NULL ,
                    UNIQUE(value))
 */

/**
 * DAO to map a custom column object in the database to a Java object so that it can be returned RESTfully.
 * <p>Custom columns are stored in the following schema:</p>
 * <style>table, th, td {border: 1px solid black;  border-collapse: collapse; padding: 5px 5px 5px 5px;} th {background-color:#DEDEDE}</style>
 * <table>
 *   <thead><tr><th>Name</th><th>Type</th><th>Description</th></tr></thead>
 *   <tbody>
 *     <tr><td>id</td><td>PRIMARY KEY</td><td>Custom column ID</td></tr>
 *     <tr><td>value</td><td>TEXT</td><td>Custom column value</td></tr>
 *   </tbody>
 * </table>
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(CustomColumnMapper.class)
public interface CustomColumnDAO {

    /**
     * Get a single custom column from the database
     *
     * @param table custom column table
     * @return An object representing a custom column
     */
    @SqlQuery("select * from custom_column_<id> ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<CustomColumn> get(@Define("id") int table,
                           @Bind("limit") int limit,
                           @Bind("offset") int offset,
                           @Define("order") String order,
                           @Define("direction") String direction
    );

    @SqlQuery("select * from custom_column_14;")
    CustomColumn get();

    @SqlQuery("Select id, custom.value " +
            "FROM books " +
            "JOIN ( " +
            "SELECT ref.id AS id, custom.value as value " +
            "FROM books ref " +
            "INNER JOIN custom_column_<table>_link AS custom_column ON custom_column.book=ref.id " +
            "INNER JOIN custom_column_<table> AS custom ON custom.id=custom_column.book " +
            "GROUP BY ref.id, custom.value " +
            ") custom USING(id) WHERE books.id =:id")
    CustomColumn get(@Define("table") int table, @Bind int id);

    /**
     * Add a new row into a custom column table
     *
     * @param table custom column table
     * @param customColumn Data to add to the table
     * @return row id for the newly created language
     */
    @SqlUpdate("INSERT INTO <table> (id, value) values (:id, :value)")
    @GetGeneratedKeys
    long insert(@Define("table") String table, @BindBean CustomColumn customColumn);

    /**
     * Create a new custom column table
     *
     * @param table name of the table to create
     */
    @SqlUpdate("create table if not exists <table> (id SERIAL PRIMARY KEY, value TEXT NOT NULL, UNIQUE(value))")
    void create(@Define("table") String table);

    /**
     * Delete all data from the custom column table, this is used when re-creating the database contents.
     * Use with caution.
     *
     * @param table Name of the table to clear
     */
    @SqlUpdate("DELETE FROM <table>")
    void purge(@Define("table") String table);

    /**
     * Drop the custom column table, this is used when re-creating the database contents.
     * Use with caution.
     *
     * @param table Name of the table to drop
     */
    @SqlUpdate("DROP TABLE <table>")
    void drop(@Define("table") String table);


}
