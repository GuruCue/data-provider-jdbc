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

import com.gurucue.recommendations.data.DataLink;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Adds methods specific to JDBC to the <code>DataLink</code> interface.
 */
public interface JdbcDataLink extends DataLink {
    /**
     * Test to see if this link is connected to the database and thus valid.
     * Invalid links should be discarded, they cannot be used anymore.
     *
     * @return whether the link is still usable
     */
    boolean isValid();

    /**
     * Set the underlying database connection into the read-only or read-write mode.
     *
     * @param readOnly if true, then the link will operate in read-only mode, otherwise it will operate in read-write mode
     */
    void setReadOnly(boolean readOnly);

    /**
     * Prepares the given SQL statement and returns the associated PreparedStatement instance.
     * It may contain one or more
     *
     * @param sql SQL statement to prepare
     * @return the prepared statement containing the pre-compiled SQL statement
     * @see java.sql.Connection#prepareStatement(String)
     */
    PreparedStatement prepareStatement(String sql);

    /**
     * Prepares the given SQL statement and returns the associated PreparedStatement instance.
     *
     * @param sql sql SQL statement to prepare
     * @param resultSetType a result set type; one of <code>ResultSet.TYPE_FORWARD_ONLY</code>, <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of <code>ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE</code>
     * @return the prepared statement containing the pre-compiled SQL statement
     * @see java.sql.Connection#prepareStatement(String, int, int)
     */
    PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency);

    /**
     * Creates a Statement object for execution of SQL statements.
     *
     * @return Statement object to execute SQL statements with
     * @see java.sql.Connection#createStatement()
     */
    Statement createStatement();

    /**
     * Creates a Statement object for execution of SQL statements.
     *
     * @param resultSetType a result set type; one of <code>ResultSet.TYPE_FORWARD_ONLY</code>, <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency a concurrency type; one of <code>ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE</code>
     * @return Statement object to execute SQL statements with
     * @see java.sql.Connection#createStatement(int, int)
     */
    Statement createStatement(int resultSetType, int resultSetConcurrency);

    /**
     * A shorthand to directly execute given SQL statement using a Statement object.
     * It is equivalent to creating a new statement, executing the given SQL with it, and closing the statement.
     * It is not intended to be used with select SQL statement.
     *
     * @param sql SQL statement to execute
     */
    void execute(String sql);

    /**
     * Prepares the given SQL call and returns the associated CallableStatement instance.
     * @param sql the SQL statement containing a function invocation
     * @return the CallableStatement using the given SQL statement
     */
    CallableStatement prepareCall(String sql);

    /**
     * Returns the underlying JDBC {@link Connection}.
     * @return the underlying Connection
     */
    Connection getConnection();
}
