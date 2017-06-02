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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcResultSet implements AutoCloseable {
    final ResultSet resultSet;

    JdbcResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close the result set: " + e.toString(), e);
        }
    }

    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to step to the next row of the result set: " + e.toString(), e);
        }
    }

    public boolean wasNull() {
        try {
            return resultSet.wasNull();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check whether the last value fetch from the result set was null: " + e.toString(), e);
        }
    }

    public long getLong(final int columnIndex) {
        try {
            return resultSet.getLong(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get a long value from the result set: " + e.toString(), e);
        }
    }

    public int getInt(final int columnIndex) {
        try {
            return resultSet.getInt(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get an int value from the result set: " + e.toString(), e);
        }
    }

    public short getShort(final int columnIndex) {
        try {
            return resultSet.getShort(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get a short value from the result set: " + e.toString(), e);
        }
    }

    public double getDouble(final int columnIndex) {
        try {
            return resultSet.getDouble(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get a double value from the result set: " + e.toString(), e);
        }
    }

    public String getString(final int columnIndex) {
        try {
            return resultSet.getString(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get a string value from the result set: " + e.toString(), e);
        }
    }

    public Timestamp getTimestamp(final int columnIndex) {
        try {
            return resultSet.getTimestamp(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get a timestamp value from the result set: " + e.toString(), e);
        }
    }
}
