/*
 * This file is part of Guru Cue Search & Recommendation Engine.
 * Copyright (C) 2017 Guru Cue Ltd.
 *
 * Guru Cue Search & Recommendation Engine is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Guru Cue Search & Recommendation Engine is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Guru Cue Search & Recommendation Engine. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.gurucue.recommendations.data.jdbc;

import com.gurucue.recommendations.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PreparedStatementPool implements AutoCloseable {
    private static final Logger log = LogManager.getLogger(PreparedStatementPool.class);
    protected final JdbcDataProvider provider;
    protected final String sql;
    private final Queue<JdbcStatement> pool = new ConcurrentLinkedQueue<JdbcStatement>();

    public PreparedStatementPool(final JdbcDataProvider provider, final String sql) {
        this.provider = provider;
        this.sql = sql;
    }

    public JdbcStatement getStatement() {
        final JdbcStatement s = pool.poll();
        if ((null != s) && (s.link.isValid())) {
            return s;
        }
        return new JdbcStatement(this);
    }

    protected void returnStatement(final JdbcStatement jdbcStatement) {
        pool.add(jdbcStatement);
    }

    @Override
    public void close() {
        JdbcStatement s;
        while ((s = pool.poll()) != null) s.closeConnection();
    }

    void statementClosed(final JdbcStatement statement) {
        provider.runAsync(new ConnectionResubmitter(this, statement));
    }

    static class ConnectionResubmitter implements Runnable {
        final PreparedStatementPool owner;
        final JdbcStatement jdbcStatement;

        ConnectionResubmitter(final PreparedStatementPool owner, final JdbcStatement jdbcStatement) {
            this.owner = owner;
            this.jdbcStatement = jdbcStatement;
        }

        @Override
        public void run() {
            // do a commit-prepare step before the statement is resubmitted, so the database can collect garbage
            try {
                jdbcStatement.preparedStatement.close();
                jdbcStatement.link.commit();
                jdbcStatement.preparedStatement = jdbcStatement.link.prepareStatement(owner.sql);
                owner.returnStatement(jdbcStatement);
            }
            catch (SQLException e) {
                log.error("Failed to close a prepared statement: " + e.toString(), e);
                jdbcStatement.link.close(); // never reuse the connection
            }
            catch (DatabaseException e) {
                log.error("Failed to commit a database connection: " + e.toString(), e);
                jdbcStatement.link.close(); // never reuse the connection
            }
        }
    }
}
