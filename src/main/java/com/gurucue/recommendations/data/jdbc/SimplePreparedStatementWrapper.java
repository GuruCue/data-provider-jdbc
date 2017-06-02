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

public class SimplePreparedStatementWrapper<R, P> {
    private static final Logger log = LogManager.getLogger(SimplePreparedStatementWrapper.class);
    private final String sql;
    private final ResultProcessor<R, P> resultProcessor;
    private final PreparedStatementParameterizer<P> parameterizer;
    private final boolean doCommit;

    public SimplePreparedStatementWrapper(final String sql, final PreparedStatementParameterizer<P> parameterizer, final ResultProcessor<R, P> resultProcessor, boolean doCommit) {
        this.sql = sql;
        this.resultProcessor = resultProcessor;
        this.parameterizer = parameterizer;
        this.doCommit = doCommit;
    }

    public SimplePreparedStatementWrapper(final String sql, final PreparedStatementParameterizer<P> parameterizer, final ResultProcessor<R, P> resultProcessor) {
        this(sql, parameterizer, resultProcessor, false);
    }

    public SimplePreparedStatementWrapper(final String sql, final PreparedStatementParameterizer<P> parameterizer) {
        this(sql, parameterizer, null, false);
    }

    public R execute(final JdbcDataLink link, final P parameter) {
        try {
            final PreparedStatement stmt = link.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            try {
                parameterizer.execute(stmt, parameter);
                if (stmt.execute() && (null != resultProcessor)) {
                    final ResultSet rs = stmt.getResultSet();
                    try {
                        return resultProcessor.process(rs, parameter);
                    }
                    finally {
                        rs.close();
                    }
                }
                return null;
            }
            finally {
                stmt.close();
            }
        } catch (SQLException se) {
            final String reason = "Database operation error: " + se.toString();
            log.error(reason, se);
            throw new DatabaseException(reason, se);
        }
    }

    public R execute(final P parameter) {
        final DataLink link = DataManager.getNewLink();
        if (!(link instanceof JdbcDataLink)) throw new DatabaseException("Cannot execute SQL statement: the data link does not implement JDBC interface");
        try {
            final R result;
            try {
                result = execute((JdbcDataLink)link, parameter);
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
