package org.async.jdbc;

import java.sql.SQLException;

/**
 * Created by panguansen on 17/9/17.
 */
public interface Connection {
    /**
     * @param SQL
     * @throws SQLException
     */
    void query(String query) throws SQLException;

    /**
     * @param SQL
     * @throws SQLException
     */
    void update(String query) throws SQLException;


}