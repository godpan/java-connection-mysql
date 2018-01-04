package org.async.jdbc;

/**
 * Created by panguansen on 17/9/17.
 */
public interface ConnectionPool {

    AsyncConnection getConnection();
}