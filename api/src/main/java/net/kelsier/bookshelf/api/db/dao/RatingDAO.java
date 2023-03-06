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

import net.kelsier.bookshelf.api.db.mapper.RatingMapper;
import net.kelsier.bookshelf.api.db.model.Rating;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/*
 * CREATE TABLE ratings (
 *     id SERIAL PRIMARY KEY,
 *     rating INTEGER CHECK(rating > -1 AND rating < 11),
 *     UNIQUE (rating)
 * )
 */

/**
 *
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@RegisterRowMapper(RatingMapper.class)
public interface RatingDAO {
    @SqlQuery("SELECT * FROM ratings WHERE ID = :id")
    Rating get(@Bind("id") int id);

    @SqlUpdate("INSERT INTO ratings (rating) " +
            "values (:rating)")
    @GetGeneratedKeys
    void insert(@BindBean Rating rating);

    @SqlQuery("SELECT * FROM ratings ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Rating> find(
            @Bind("limit") int limit,
            @Bind("offset") int offset,
            @Define("order") String order,
            @Define("direction") String direction
    );

    @SqlQuery("SELECT * FROM ratings WHERE <column> <clause> :value ORDER BY <order> <direction> LIMIT :limit OFFSET :offset")
    List<Rating> find(@Bind("value") int value,
                      @Define("column") final String column,
                      @Define("clause") final String clause,
                      @Bind("limit") int limit,
                      @Bind("offset") int offset,
                      @Define("order") String order,
                      @Define("direction") String direction
    );

    @SqlUpdate("DELETE FROM ratings")
    void purge();
}
