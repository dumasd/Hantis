package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractTypeHandler<T> implements TypeHandler<T> {
	
	private Class<T> type;
	
    public AbstractTypeHandler(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement stat, int pos, Object parameter, JDBCType type) throws SQLException {
        if (parameter == null) {
            if (type == null) {
                throw new SQLException("The JDBCType can't be null when the parameter is null");
            }
            stat.setNull(pos, type.getType());
        } else {
            setNotNullParameter(stat, pos, (T) parameter);
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        T result;
        try {
            result = getNotNullResult(rs, columnIndex);
        } catch (Throwable e) {
            throw new SQLException(e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        T result;
        try {
            result = getNotNullResult(rs, columnName);
        } catch (Throwable e) {
            throw new SQLException(e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }
    
    @Override
    public Class<T> getType() {
    	return type;
    }
    
    protected abstract void setNotNullParameter(PreparedStatement stat, int pos, T parameter) throws SQLException;

    protected abstract T getNotNullResult(ResultSet rs, int columnIndex) throws SQLException;

    protected abstract T getNotNullResult(ResultSet rs, String columnName) throws SQLException;

}
