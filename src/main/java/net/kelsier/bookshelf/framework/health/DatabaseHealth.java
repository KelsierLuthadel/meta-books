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

package net.kelsier.bookshelf.framework.health;

import com.codahale.metrics.health.HealthCheck;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;

/**
 * Database health check for Dropwizard
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class DatabaseHealth extends HealthCheck {

    /**
     * An instance of a DAO
     */
    final RoleDAO roleDAO;

    /**
     * Constructor
     *
     * @param roleDAO An instance of a DAO
     */
    public DatabaseHealth(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    /**
     * The check to be performed
     *
     * @return Healthy if there was no issue. Refer to Dropwizard for response status
     */
    @Override
    protected Result check() {
        try {
            roleDAO.getAll();
            return Result.healthy();
        } catch (final Exception e) {
            return Result.unhealthy("Database connection failed");
        }
    }
}
