package com.thinkerwolf.hantis.orm;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.Params;
import com.thinkerwolf.hantis.common.util.ReflectionUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.orm.annotation.Column;
import com.thinkerwolf.hantis.orm.annotation.Entity;
import com.thinkerwolf.hantis.orm.annotation.Id;
import com.thinkerwolf.hantis.orm.annotation.IngnoreField;
import com.thinkerwolf.hantis.sql.Sql;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * 一个orm实体对象
 * 
 * @author wukai
 *
 */
public class TableEntity<T> implements Closeable {

	/** Orm class */
	private Class<T> clazz;
	/** name handler */
	private NameHandler nameHandler;
	/** 数据库表名 */
	private String tableName;
	/** 列 columnName:TableColumn */
	private Map<String, TableColumn> tableColumnMap = new HashMap<>();

	private String updateSql;

	public TableEntity(Class<T> clazz, NameHandler nameHandler) {
		this.clazz = clazz;
		this.nameHandler = nameHandler;
		init();
	}

	private void init() {
		Entity entity = ReflectionUtils.getAnnotation(clazz, Entity.class);
		if (StringUtils.isNotEmpty(entity.name())) {
			tableName = entity.name();
		} else {
			tableName = nameHandler.convertToColumnName(clazz.getSimpleName());
		}

		boolean hasId = false;
		for (Field field : clazz.getFields()) {
			Id id = ReflectionUtils.getAnnotation(field, Id.class);
			if (id != null) {
				String columnName = createColumnName(field);
				tableColumnMap.put(columnName, new TableColumn(field, columnName, true));
			} else {
				if (ReflectionUtils.getAnnotation(field, IngnoreField.class) == null) {
					String columnName = createColumnName(field);
					tableColumnMap.put(columnName, new TableColumn(field, columnName));
				}
			}
		}
	}

	private String generateUpdateSql() {
		StringBuilder sb = new StringBuilder("UPDATE " + tableName + " SET ");
		int index = 1;
		for (TableColumn tc : tableColumnMap.values()) {
			if (index != 1) {
				sb.append(",");
			}
			sb.append(tc.getColumnName() + " = ? ");
			index++;
		}
		sb.append("WHERE ");
		index = 1;
		for (TableColumn tc : tableColumnMap.values()) {

			index++;
		}
		return sb.toString();
	}

	public Sql parseSelectSql(Object parameter) {
		Sql sql = new Sql(parameter);
		sql.appendSql("SELECT * FROM " + tableName);
		sql.setReturnType(clazz);
		Params params = new Params();
		StringBuilder condition = new StringBuilder();
		boolean first = true;
		for (TableColumn tc : tableColumnMap.values()) {
			try {
				Object value = Ognl.getValue(tc.getFieldName(), parameter);
				if (value != null) {
					params.add(new Param(value));
					if (first) {
						first = false;
						condition.append(tc.getColumnName() + " = ? ");
					} else {
						condition.append("AND " + tc.getColumnName() + " = ? ");
					}
				}
			} catch (OgnlException e) {
				// Ingore
			}
		}
		if (params.size() > 0) {
			sql.appendSql(" WHERE ").appendSql(condition.toString()).appendParams(params);
		}
		return sql;
	}

	public Sql parseUpdateSql(T oldEntity, T entity) {
		if (entity.getClass() != clazz) {
			throw new RuntimeException("Parm is not the type of " + clazz.getName());
		}
		Sql sql = new Sql(null);
		sql.appendSql("UPDATE " + tableName + " SET ");
		sql.setReturnType(void.class);
		TableColumn idColumn = null;
		for (TableColumn tc : tableColumnMap.values()) {
			sql.appendSql(tc.getColumnName() + " = ?, ");
			sql.appendParam(new Param(tc.getValue(entity)));
			if (tc.isPrimaryKey()) {
				idColumn = tc;
			}
		}
		return sql;
	}

	public Sql generateDeleteSql(T entity) {
		if (entity.getClass() != clazz) {
			throw new RuntimeException("Parm is not the type of " + clazz.getName());
		}
		Sql sql = new Sql(null);
		sql.appendSql("DELETE FROM " + tableName + " SET ");
		sql.setReturnType(void.class);
		for (TableColumn tc : tableColumnMap.values()) {
			sql.appendSql(tc.getColumnName() + " = ? ");
			sql.appendParam(new Param(tc.getValue(entity)));
		}
		return sql;
	}

	private String createColumnName(Field field) {
		Column column = ReflectionUtils.getAnnotation(clazz, Column.class);
		if (column == null || StringUtils.isEmpty(column.name())) {
			return nameHandler.convertToColumnName(field.getName());
		}
		return column.name();
	}

	@Override
	public void close() throws IOException {

	}

}
