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

import net.kelsier.bookshelf.api.db.mapper.CustomColumnsMapper;
import net.kelsier.bookshelf.api.db.model.CustomColumns;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/*
 * CREATE TABLE custom_columns (
 *     id SERIAL PRIMARY KEY,
 *     label TEXT NOT NULL,
 *     name TEXT NOT NULL,
 *     datatype TEXT NOT NULL,
 *     display  TEXT DEFAULT '{}' NOT NULL,
 *     is_multiple BOOL DEFAULT false NOT NULL,
 *     normalized BOOL NOT NULL,
 *     UNIQUE(label)
 * )
 */

/**
 * DAO for users in a database
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(CustomColumnsMapper.class)
public interface CustomColumnsDAO {

    @SqlQuery("SELECT * FROM custom_columns")
    List<CustomColumns> get();

    @SqlQuery("SELECT * FROM custom_columns WHERE ID = :id")
    CustomColumns get(@Bind("id") int id);

    @SqlUpdate("INSERT INTO custom_columns (label, name, datatype, display, is_multiple, normalized) " +
            "values (:label, :name, :dataType, :display, :multiple, :normalized)")
    @GetGeneratedKeys
    long insert(@BindBean CustomColumns bookTagLink);

    @SqlUpdate("DELETE FROM custom_columns")
    void purge();

}
