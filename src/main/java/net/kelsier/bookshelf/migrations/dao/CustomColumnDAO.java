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

package net.kelsier.bookshelf.migrations.dao;

import net.kelsier.bookshelf.migrations.mapper.CustomColumnMapper;
import net.kelsier.bookshelf.migrations.model.Comment;
import net.kelsier.bookshelf.migrations.model.CustomColumn;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/**
 * DAO for users in a database
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(CustomColumnMapper.class)
public interface CustomColumnDAO {

    /**
     *
     * @return
     */
    @SqlQuery("select * from <table>")
    CustomColumn get(@Define("table") String table);

    @SqlUpdate("create table if not exists <table> (id SERIAL PRIMARY KEY, value TEXT NOT NULL, UNIQUE(value))")
    void create(@Define("table") String table);

    @SqlUpdate("INSERT INTO <table> (value) " +
            "values (:value)")
    @GetGeneratedKeys
    long insert(@Define("table") String table, @BindBean CustomColumn customColumn);

    @SqlUpdate("DELETE FROM <table>")
    void purge(@Define("table") String table);

    @SqlUpdate("DROP TABLE <table>")
    void drop(@Define("table") String table);


}
