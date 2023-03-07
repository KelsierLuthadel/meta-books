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

import net.kelsier.bookshelf.api.db.mapper.CommentMapper;
import net.kelsier.bookshelf.api.db.model.Comment;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/*
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    text TEXT NOT NULL ,
    UNIQUE(book)
)
 */

/**
 * DAO to map a comment object in the database to a Java object so that it can be returned RESTfully.
 * <p>Comments are stored in the following schema:</p>
 * <style>table, th, td {border: 1px solid black;  border-collapse: collapse; padding: 5px 5px 5px 5px;} th {background-color:#DEDEDE}</style>
 * <table>
 *   <thead><tr><th>Name</th><th>Type</th><th>Description</th></tr></thead>
 *   <tbody>
 *     <tr><td>id</td><td>PRIMARY KEY</td><td>Comment ID</td></tr>
 *     <tr><td>book</td><td>INTEGER</td><td>Book ID linking to this comments</td></tr>
 *     <tr><td>text</td><td>TEXT</td><td>Comment text</td></tr>
 *     <tr><td>sort</td><td>TEXT</td><td>Used for sorting authors</td></tr>
 *     <tr><td>sort</td><td>TEXT</td><td>Used for sorting authors</td></tr>
 *   </tbody>
 * </table>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@RegisterRowMapper(CommentMapper.class)
public interface CommentDAO {
    @SqlQuery("SELECT * FROM comments WHERE ID = :id")
    Comment get(@Bind("id") int id);

    @SqlQuery("SELECT * FROM comments ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Comment> find(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlQuery("SELECT * FROM comments WHERE comments.text ILIKE :text ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Comment> find(
            @Bind("text") String text,
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlQuery("SELECT * FROM comments WHERE <column> <clause> :text ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Comment> find(
            @Bind("text") String text,
            @Define("column") final String column,
            @Define("clause") final String clause,
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlUpdate("INSERT INTO comments (book, text) " +
            "values (:book, :text)")
    @GetGeneratedKeys
    void insert(@BindBean Comment comment);

    @SqlUpdate("DELETE FROM comments")
    void purge();
}
