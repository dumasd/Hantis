package com.thinkerwolf.hantis.common.type;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author wukai
 *
 */
public class TypeHandlerRegistry {

	private Map<Class<?>, Map<JDBCType, TypeHandler<?>>> classJdbcTypeHandlerMap = new HashMap<>();

	private Map<JDBCType, TypeHandler<?>> jdbcTypeHandlerMap = new HashMap<>();

	public TypeHandlerRegistry() {

		TypeHandler<?> handler;

		// boolean
		handler = new BooleanTypeHandler();
		register(boolean.class, handler);
		register(Boolean.class, handler);
		register(JDBCType.BOOLEAN, handler);
		register(JDBCType.BIT, handler);

		// byte
		handler = new ByteTypeHandler();
		register(byte.class, handler);
		register(Byte.class, handler);
		register(JDBCType.TINYINT, new ByteTypeHandler());

		// short
		handler = new ShortTypeHandler();
		register(short.class, handler);
		register(Short.class, handler);
		register(JDBCType.SMALLINT, handler);

		// int
		handler = new IntegerTypeHandler();
		register(int.class, handler);
		register(Integer.class, handler);
		register(JDBCType.INTEGER, handler);

		// long
		handler = new LongTypeHandler();
		register(long.class, handler);
		register(Long.class, handler);
		register(JDBCType.BIGINT, handler);

		// float
		handler = new FloatTypeHandler();
		register(float.class, handler);
		register(Float.class, handler);
		register(JDBCType.FLOAT, handler);

		// double
		handler = new DoubleTypeHandler();
		register(double.class, handler);
		register(Double.class, handler);
		register(JDBCType.DOUBLE, handler);

		// register(String.class, JdbcType.CHAR, new StringTypeHandler());
		// register(String.class, JdbcType.CLOB, new ClobTypeHandler());
		// register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
		// register(String.class, JdbcType.LONGVARCHAR, new ClobTypeHandler());
		// register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());
		// register(String.class, JdbcType.NCHAR, new NStringTypeHandler());
		// register(String.class, JdbcType.NCLOB, new NClobTypeHandler());
		// string
		handler = new StringTypeHandler();
		register(String.class, handler);
		register(String.class, JDBCType.CHAR, handler);
		register(String.class, JDBCType.VARCHAR, handler);
		// string clob
		register(String.class, JDBCType.CLOB, new ClobTypeHandler());
		register(String.class, JDBCType.LONGVARCHAR, new ClobTypeHandler());
		// string NString
		register(String.class, JDBCType.NVARCHAR, new NStringTypeHandler());
		register(String.class, JDBCType.NCHAR, new NStringTypeHandler());
		register(String.class, JDBCType.NCLOB, new NStringTypeHandler());

		// register(BigDecimal.class, new BigDecimalTypeHandler());
		// register(JdbcType.REAL, new BigDecimalTypeHandler());
		// register(JdbcType.DECIMAL, new BigDecimalTypeHandler());
		// register(JdbcType.NUMERIC, new BigDecimalTypeHandler());
		// bigdemical
		handler = new BigDecimalTypeHandler();
		register(BigDecimal.class, handler);
		register(JDBCType.REAL, handler);
		register(JDBCType.DECIMAL, handler);
		register(JDBCType.NUMERIC, handler);

		// Byte[]
		handler = new BlobByteTypeHandler();
		register(Byte[].class, handler);
		register(Byte[].class, JDBCType.LONGVARBINARY, handler);
		register(Byte[].class, JDBCType.BLOB, handler);

		// byte[]
		handler = new BlobTypeHandler();
		register(byte[].class, handler);
		register(byte[].class, JDBCType.LONGVARBINARY, handler);
		register(byte[].class, JDBCType.BLOB, handler);

		// date
		handler = new DateTypeHandler();
		register(Date.class, handler);
		register(JDBCType.DATE, handler);
		register(java.sql.Date.class, new SqlDateTypeHandler());

		// timestamp
		handler = new TimestampTypeHandler();
		register(Timestamp.class, handler);
		register(JDBCType.TIMESTAMP, handler);

		// time
		handler = new TimeTypeHandler();
		register(Time.class, handler);
		register(JDBCType.TIME, handler);

		// char
		register(Character.class, new CharacterTypeHandler());
		register(char.class, new CharacterTypeHandler());

	}

	public void register(Class<?> clazz, TypeHandler<?> handler) {
		register(clazz, null, handler);
	}

	public void register(Class<?> clazz, JDBCType jdbcType, TypeHandler<?> handler) {
		Map<JDBCType, TypeHandler<?>> map = classJdbcTypeHandlerMap.get(clazz);
		if (map == null) {
			map = new HashMap<>();
			classJdbcTypeHandlerMap.put(clazz, map);
		}
		map.put(jdbcType, handler);
		if (jdbcType != null) {
			jdbcTypeHandlerMap.put(jdbcType, handler);
		}
	}

	public void register(JDBCType jdbcType, TypeHandler<?> handler) {
		jdbcTypeHandlerMap.put(jdbcType, handler);
	}

	public <T> TypeHandler<T> getHandler(Class<?> clazz) {
		return getHandler(clazz, null);
	}

	@SuppressWarnings("unchecked")
	public <T> TypeHandler<T> getHandler(Class<?> clazz, JDBCType jdbcType) {
		Map<JDBCType, TypeHandler<?>> map = classJdbcTypeHandlerMap.get(clazz);
		if (map == null) {
			return (TypeHandler<T>) jdbcTypeHandlerMap.get(jdbcType);
		}
		TypeHandler<T> handler = (TypeHandler<T>) map.get(jdbcType);
		if (handler == null) {
			handler = (TypeHandler<T>) map.get((JDBCType) null);
		}
		return handler;
	}
	
	@SuppressWarnings("unchecked")
	public <T> TypeHandler<T> getHandler(JDBCType jdbcType) {
		return (TypeHandler<T>) jdbcTypeHandlerMap.get(jdbcType);
	}
}
