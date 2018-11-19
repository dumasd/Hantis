package com.thinkerwolf.hantis.transaction;

import java.sql.Connection;

/**
 * 资源holder
 */
public interface ResourceHolder {

    Object getResource();

    Connection getConnection();

    void setConnection(Connection connection);
}
