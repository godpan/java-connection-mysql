package org.async.jdbc;

import java.sql.SQLException;

/**
 * Created by panguansen on 17/9/17.
 */
public interface Callback {
    /**
     * @param SQLException
     */
    void onError(SQLException e);
}