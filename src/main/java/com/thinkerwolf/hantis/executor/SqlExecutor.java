package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.transaction.TransactionSychronizationManager;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcTransactionManager.JdbcResourceHolder;
import com.thinkerwolf.hantis.type.JDBCType;
import com.thinkerwolf.hantis.type.TypeHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql执行器
 *
 * @author wukai
 */
@Deprecated
public class SqlExecutor {

    private DataSource dataSource;

    private NameHandler nameHandler = new DefaultNameHandler();

    private Configuration configuration;

    public <T> List<T> queryForList(String sql, List<Param> params, Class<T> clazz) {
        Connection connection = getConnection();
        ResultSetListHandler<T> listHandler = new ResultSetListHandler<>(new ClassRowHander<>(clazz, nameHandler));
        PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql, params);
        return execute(new QueryStatementExecuteCallback<>(builder, listHandler));
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        return queryForList(sql, Collections.emptyList(), clazz);
    }

    public <T> T queryForOne(String sql, List<Param> params, Class<T> clazz) {
        List<T> l = queryForList(sql, params, clazz);
        if (l.size() == 0) {
            return null;
        }
        if (l.size() > 1) {
            throw new RuntimeException("");
        }
        return l.get(0);
    }

    public List<Map<String, Object>> queryForList(String sql, List<Param> params) {
        ResultSetListHandler<Map<String, Object>> listHandler = new ResultSetListHandler<>(new MapRowHandler());
        Connection connection = getConnection();
        PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql, params);
        return execute(new QueryStatementExecuteCallback<>(builder, listHandler));
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return queryForList(sql, Collections.emptyList());
    }

    public Map<String, Object> queryForOne(String sql, List<Param> params) {
        List<Map<String, Object>> l = queryForList(sql, params);
        if (l.size() == 0) {
            return null;
        }
        if (l.size() > 1) {
            throw new RuntimeException("");
        }
        return l.get(0);
    }

    public <T> T execute(StatementExecuteCallback<T> callback) {
        return callback.execute();
    }

    /**
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, List<Param> params) {
        Connection connection = getConnection();
        PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql, params);
        StatementExecuteCallback<Integer> callback = new StatementExecuteCallback<Integer>() {
            @Override
            public Integer execute() {
                try {
                    PreparedStatement ps = builder.build();
                    return ps.executeUpdate();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return execute(callback);
    }

    protected Connection getConnection() {
        JdbcResourceHolder resourceHolder = (JdbcResourceHolder) TransactionSychronizationManager
                .getResource(dataSource);
        if (resourceHolder != null) {
            return resourceHolder.getConnection();
        } else {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private static class ClassRowHander<T> implements RowHandler<T> {
        private Class<T> clazz;
        private NameHandler nameHandler;

        public ClassRowHander(Class<T> clazz, NameHandler nameHandler) {
            this.clazz = clazz;
            this.nameHandler = nameHandler;
        }

        @Override
        public T processRow(ResultSet rs) throws Throwable {
            T t = clazz.newInstance();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            for (int i = 0; i < count; i++) {
                Object obj = rs.getObject(i + 1);
                String columnName = meta.getColumnName(i + 1);
                PropertyUtils.setProperty(t, nameHandler.convertToPropertyName(columnName), obj);
            }
            return t;
        }
    }

    private static class MapRowHandler implements RowHandler<Map<String, Object>> {
        @Override
        public Map<String, Object> processRow(ResultSet rs) throws Throwable {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            for (int i = 0; i < count; i++) {
                Object obj = rs.getObject(i + 1);
                String columnName = meta.getColumnName(i + 1);
                map.put(columnName, obj);
            }
            return map;
        }
    }

    private static class QueryStatementExecuteCallback<T> implements StatementExecuteCallback<T> {
        private PreparedStatementBuilder builder;
        private ResultSetHandler<T> listHandler;

        public QueryStatementExecuteCallback(PreparedStatementBuilder builder, ResultSetHandler<T> listHandler) {
            this.builder = builder;
            this.listHandler = listHandler;
        }

        @Override
        public T execute() {
            try {
                PreparedStatement ps = builder.build();
                ResultSet rs = ps.executeQuery();
                return listHandler.process(rs);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class PreparedStatementBuilderImpl implements PreparedStatementBuilder {

        private Connection connection;
        private String sql;
        private List<Param> params;

        public PreparedStatementBuilderImpl(Connection connection, String sql, List<Param> params) {
            this.connection = connection;
            this.sql = sql;
            this.params = params;
        }

        @Override
        public PreparedStatement build() throws Throwable {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Param param = params.get(i);
                    TypeHandler<?> handler;
                    if (param.getType() != JDBCType.UNKONWN) {
                        handler = configuration.getTypeHandlerRegistry().getHandler(param.getType());
                    } else {
                        handler = configuration.getTypeHandlerRegistry().getHandler(param.getValue().getClass());
                    }
                    handler.setParameter(ps, i + 1, param.getValue(), param.getType());
                }
            }
            return ps;
        }
    }

}
