package com.thinkerwolf.hantis.type;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobTypeHandler extends AbstractTypeHandler<String> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, String parameter) throws SQLException {
        stat.setCharacterStream(pos, new StringReader(parameter));
    }

    @Override
    protected String getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            Reader reader = rs.getCharacterStream(columnIndex);
            StringBuilder sb = new StringBuilder();
            char[] cbuf = new char[1024 * 2];
            int n = 0;
            while (-1 != (n = reader.read(cbuf))) {
                sb.append(cbuf, 0, n);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    protected String getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        try {
            Reader reader = rs.getCharacterStream(columnName);
            StringBuilder sb = new StringBuilder();
            char[] cbuf = new char[1024 * 2];
            int n = 0;
            while (-1 != (n = reader.read(cbuf))) {
                sb.append(cbuf, 0, n);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

}