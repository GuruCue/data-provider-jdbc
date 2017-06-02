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
import com.gurucue.recommendations.data.DataLink;
import com.gurucue.recommendations.data.DataManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementWrapper<R, P> {
    private static final Logger log = LogManager.getLogger(PreparedStatementWrapper.class);
    private final String logPrefix;
    private final PreparedStatementProcessor<R, P> statementProcessor;

    public PreparedStatementWrapper(final String logPrefix, final PreparedStatementProcessor<R, P> statementProcessor) {
        this.logPrefix = logPrefix;
        this.statementProcessor = statementProcessor;
    }

    public R execute(final JdbcDataLink link, final String sql, final P parameters) {
        try {
            final PreparedStatement stmt = link.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            try {
                return statementProcessor.process(stmt, parameters);
            }
            finally {
                stmt.close();
            }
        } catch (SQLException se) {
            final String reason = logPrefix + " Database operation error: " + se.toString() + "; SQL: " + sql;
            log.error(reason, se);
            throw new DatabaseException(reason, se);
        }
    }

    public R execute(final String sql, final P parameters) {
        final DataLink link = DataManager.getNewLink();
        if (!(link instanceof JdbcDataLink)) throw new DatabaseException("Cannot execute SQL statement: the data link does not implement JDBC interface");
        try {
            final R result;
            try {
                result =  execute((JdbcDataLink)link, sql, parameters);
            }
            catch (RuntimeException e) {
                link.rollback();
                throw e;
            }
            link.commit();
            return result;
        }
        finally {
            link.close();
        }
    }
}
