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

import net.kelsier.bookshelf.framework.db.mapper.bookshelf.AuthorMapper;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Author;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
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
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(AuthorMapper.class)
public interface AuthorDAO {
    @SqlQuery("SELECT * FROM authors WHERE ID = :id")
    Author get(@Bind("id") int id);

    @SqlUpdate("INSERT INTO authors (name, sort) " +
            "values (:name, :sort)")
    @GetGeneratedKeys
    long insert(@BindBean Author author);

    @SqlQuery("SELECT * FROM authors ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Author> get(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlQuery("SELECT * FROM authors WHERE authors.name ILIKE :name LIMIT :limit OFFSET :offset")
    List<Author> findByTitle(@Bind("name") String name, @Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM authors WHERE <column> <clause> :text ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Author> find(
            @Bind("text") String text,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit, @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlUpdate("DELETE FROM authors")
    void purge();
}
