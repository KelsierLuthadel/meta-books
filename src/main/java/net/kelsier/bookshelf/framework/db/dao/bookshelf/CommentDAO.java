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

import net.kelsier.bookshelf.framework.db.mapper.bookshelf.CommentMapper;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
import net.kelsier.bookshelf.framework.db.model.bookshelf.BookData;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Comment;
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
);
 */

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(CommentMapper.class)
public interface CommentDAO {
    @SqlQuery("SELECT * FROM comments WHERE ID = :id")
    Comment get(@Bind("id") int id);

    @SqlQuery("SELECT * FROM comments LIMIT :limit OFFSET :offset")
    List<Comment> find(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM comments WHERE comments.text ILIKE :text LIMIT :limit OFFSET :offset")
    List<Comment> find(@Bind("text") String text, @Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM comments WHERE <column> ILIKE :text LIMIT :limit OFFSET :offset")
    List<Comment> find(@Bind("text") String text, @Define("column") final String column, @Bind("limit") int limit, @Bind("offset") int offset);

    @SqlUpdate("INSERT INTO comments (book, text) " +
            "values (:book, :text)")
    @GetGeneratedKeys
    long insert(@BindBean Comment comment);

    @SqlUpdate("DELETE FROM comments")
    void purge();
}
