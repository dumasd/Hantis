package com.thinkerwolf.hantis.orm;

import java.lang.reflect.Field;

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

	public TableColumn(Field field, String columnName) {
		this(field, columnName, false);
	}

	public TableColumn(Field field, String columnName, boolean primaryKey) {
		this.field = field;
		this.columnName = columnName;
		this.primaryKey = primaryKey;
		this.fieldName = field.getName();
		init();
	}

	private void init() {

	}

	public void setValue(Object entity, Object value) {
		try {
			field.set(entity, value);
		} catch (Exception e) {
			throw new RuntimeException("Field set error", e);
		}
	}
	
	public Object getValue(Object entity) {
		try {
			field.setAccessible(true);
			return field.get(entity);
		} catch (Exception e) {
			throw new RuntimeException("Field get error", e);
		} 
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

}
