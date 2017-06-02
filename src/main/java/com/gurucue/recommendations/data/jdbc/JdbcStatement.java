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

import java.sql.*;

public class JdbcStatement implements AutoCloseable {
    final PreparedStatementPool owner;
    final JdbcDataLink link;
    PreparedStatement preparedStatement;

    JdbcStatement(final PreparedStatementPool owner) {
        this.owner = owner;
        link = owner.provider.newJdbcDataLink();
        try {
            link.setReadOnly(true);
        }
        catch (RuntimeException e) {
            link.close();
            throw e;
        }
        try {
            preparedStatement = link.prepareStatement(owner.sql);
        }
        catch (RuntimeException e) {
            link.close();
            throw e;
        }
    }

    protected void closeConnection() {
        try {
            preparedStatement.close();
        }
        catch (SQLException e) {}
        link.close();
    }

    @Override
    public void close() {
        owner.statementClosed(this);
    }

    public JdbcResultSet executeQuery() {
        try {
            return new JdbcResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute a query statement: " + e.toString(), e);
        }
    }

    public void setDate(int parameterIndex, Date x) {
        try {
            preparedStatement.setDate(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set date query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setDouble(int parameterIndex, double x) {
        try {
            preparedStatement.setDouble(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set double query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setFloat(int parameterIndex, float x) {
        try {
            preparedStatement.setFloat(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set float query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setInt(int parameterIndex, int x) {
        try {
            preparedStatement.setInt(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set int query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setLong(int parameterIndex, long x) {
        try {
            preparedStatement.setLong(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set long query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setNull(int parameterIndex, int sqlType) {
        try {
            preparedStatement.setNull(parameterIndex, sqlType);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set null query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setShort(int parameterIndex, short x) {
        try {
            preparedStatement.setShort(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set short query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setString(int parameterIndex, String x) {
        try {
            preparedStatement.setString(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set string query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setTime(int parameterIndex, Time x) {
        try {
            preparedStatement.setTime(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set time query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x) {
        try {
            preparedStatement.setTimestamp(parameterIndex, x);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to set timestamp query parameter at position " + parameterIndex + ": " + e.toString(), e);
        }
    }
}
