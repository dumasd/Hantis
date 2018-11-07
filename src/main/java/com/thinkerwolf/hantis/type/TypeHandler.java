package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器 参考了MyBatis处理器的设计
 *
 * @author wukai
 */
public interface TypeHandler<T> {

    void setParameter(PreparedStatement stat, int pos, Object parameter, JDBCType type) throws SQLException;

    T getResult(ResultSet rs, String columnName) throws SQLException;

    T getResult(ResultSet rs, int columnIndex) throws SQLException;

    //T getResult(CallableStatement cs, int columnIndex) throws SQLException;
}
