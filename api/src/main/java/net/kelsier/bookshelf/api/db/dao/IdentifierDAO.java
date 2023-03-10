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

import net.kelsier.bookshelf.api.db.mapper.IdentifierMapper;
import net.kelsier.bookshelf.api.db.model.Identifier;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/*
 * CREATE TABLE identifiers (
 *     id SERIAL PRIMARY KEY,
 *     book INTEGER NOT NULL,
 *     type TEXT NOT NULL DEFAULT 'isbn' ,
 *     val  TEXT NOT NULL ,
 *     UNIQUE(book, type)
 * )
 */

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(IdentifierMapper.class)
public interface IdentifierDAO {
    @SqlQuery("SELECT * FROM identifiers WHERE ID = :id")
    Identifier get(@Bind("id") int id);

    @SqlUpdate("INSERT INTO identifiers (id, book, type, val) " +
            "values (:id, :book, :type, :val)")
    @GetGeneratedKeys
    long insert(@BindBean Identifier author);

    /**
     * Delete all identifiers from the database, this is used when re-creating the database contents.
     * Use with caution.
     */
    @SqlUpdate("DELETE FROM identifiers")
    void purge();
}
