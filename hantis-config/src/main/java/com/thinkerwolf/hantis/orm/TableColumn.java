package com.thinkerwolf.hantis.orm;

import com.thinkerwolf.hantis.common.util.ReflectionUtils;
import com.thinkerwolf.hantis.executor.Executor;
import com.thinkerwolf.hantis.orm.annotation.GeneratedValue;
import com.thinkerwolf.hantis.sql.Sql;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 一列
 * 
 * @author wukai
 *
 */
public class TableColumn {

	/** Field */
	private Field field;
	/** 数据库列名 */
	private String columnName;
	/** 是否是主键 */
	private boolean primaryKey;
	/** 属性名 */
	private String fieldName;
	/**
	 * 表名
	 */
	private String tableName;

	private GenerateStrategy<?> generateStrategy;

	private boolean generatedValue;

	public TableColumn(Field field, String columnName, String tableName) {
		this(field, columnName, tableName, false);
	}

	public TableColumn(Field field, String columnName, String tableName, boolean primaryKey) {
		this.field = field;
		this.columnName = columnName;
		this.tableName = tableName;
		this.primaryKey = primaryKey;
		this.fieldName = field.getName();
		init();
	}

	private void init() {
		GeneratedValue genValue = ReflectionUtils.getAnnotation(field, GeneratedValue.class);
		if (genValue != null) {
			this.generatedValue = true;
		}
	}

	public void setValue(Object entity, Object value) {
		try {
			field.set(entity, value);
		} catch (Exception e) {
			throw new RuntimeException("Field set error", e);
		}
	}

	public Object getValue(Executor executor, Object entity) {
		return getValue(executor, entity, false);
	}

	public Object getValue(Executor executor, Object entity, boolean autoGenerate) {
		try {
			field.setAccessible(true);
			Object value = field.get(entity);
			if (generatedValue) {
				if (autoGenerate && isNullValue(value)) {
					value = getGenerateStrategy(executor).autoGenerate();
				} else {
					getGenerateStrategy(executor).compareAndSet(value);
				}
			}
			return value;
		} catch (Exception e) {
			throw new RuntimeException("Field get error", e);
		}
	}

	public boolean isNullValue(Object value) {
		if (value == null) {
			return true;
		} else {
			Class<?> clazz = field.getType();
			if (clazz.isPrimitive()) {
				return value.equals(0);
			}
		}
		return false;
	}

	public Field getField() {
		return field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public GenerateStrategy<?> getGenerateStrategy(Executor executor) {
		if (generateStrategy == null) {
			synchronized (this) {
				if (generateStrategy == null) {
					generateStrategy = createGenerateStrategy(executor);
				}
			}
		}
		return generateStrategy;
	}

	private synchronized GenerateStrategy<?> createGenerateStrategy(Executor executor) {
		Class<?> clazz = field.getType();
		Sql sql = new Sql(null);
		sql.appendSql("SELECT MAX(" + columnName + ") as max FROM " + tableName);
		Map<String, Object> map = executor.queryForOne(sql);
		Object maxValue = map.get("max");
		GenerateStrategy<?> generateStrategy;
		if (clazz == int.class || clazz == Integer.class) {
			generateStrategy = new IntegerGenerateStrategy(maxValue == null ? 0 : (int) maxValue);
		} else if (clazz == long.class || clazz == Long.class) {
			generateStrategy = new LongGenerateStrategy(maxValue == null ? 0 : (long) maxValue);
		} else if (clazz == float.class || clazz == Float.class) {
			generateStrategy = new FloatGenerateStrategy(maxValue == null ? 0 : (float) maxValue);
		} else if (clazz == double.class || clazz == Double.class) {
			generateStrategy = new DoubleGenerateStrategy(maxValue == null ? 0 : (double) maxValue);
		} else {
			generateStrategy = new StringGenerateStrategy(10);
		}
		return generateStrategy;
	}

}
