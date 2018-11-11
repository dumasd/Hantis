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

    private String insertSql;

    private String deleteSql;

    private String selectSql;

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
        for (Field field : clazz.getDeclaredFields()) {
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
        this.selectSql = generateSelectSql();
        this.updateSql = generateUpdateSql();
        this.deleteSql = generateDeleteSql();
        this.insertSql = generateInsertSql();

    }

    private String generateSelectSql() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);
        return sb.toString();

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
        sb.append(" WHERE ");
        index = 1;
		for (TableColumn tc : tableColumnMap.values()) {
            if (tc.isPrimaryKey()) {
                if (index != 1) {
                    sb.append("AND ");
                }
                sb.append(tc.getColumnName()).append(" = ? ");
                index++;
            }
        }
		return sb.toString();
	}


    private String generateDeleteSql() {
        StringBuilder sb = new StringBuilder("DELETE FROM " + tableName);
        sb.append(" WHERE ");
        int index = 1;
        for (TableColumn tc : tableColumnMap.values()) {
            if (tc.isPrimaryKey()) {
                if (index != 1) {
                    sb.append("AND ");
                }
                sb.append(tc.getColumnName());
                sb.append(" = ? ");
                index++;
            }
        }
        return sb.toString();
    }

    private String generateInsertSql() {
        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName);
        sb.append(" (");
        int index = 1;
        for (TableColumn tc : tableColumnMap.values()) {
            if (index != 1) {
                sb.append(", ");
            }
            sb.append(tc.getColumnName());
            index++;
        }
        sb.append(") VALUES (");

        for (int i = 0; i < tableColumnMap.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }



	public Sql parseSelectSql(Object parameter) {
		Sql sql = new Sql(parameter);
        sql.appendSql(this.selectSql);
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

    public Sql parseUpdateSql(Object entity) {
        if (entity.getClass() != clazz) {
			throw new RuntimeException("Parm is not the type of " + clazz.getName());
		}
		Sql sql = new Sql(null);
        sql.appendSql(this.updateSql);
        sql.setReturnType(void.class);
		for (TableColumn tc : tableColumnMap.values()) {
			sql.appendParam(new Param(tc.getValue(entity)));
		}
        for (TableColumn tc : tableColumnMap.values()) {
            if (tc.isPrimaryKey()) {
                sql.appendParam(new Param(tc.getValue(entity)));
            }
        }
        return sql;
	}

    public Sql parseDeleteSql(Object entity) {
        if (entity.getClass() != clazz) {
            throw new RuntimeException("Parm is not the type of " + clazz.getName());
        }
        Sql sql = new Sql(null);
        sql.appendSql(this.deleteSql);
        sql.setReturnType(void.class);
        for (TableColumn tc : tableColumnMap.values()) {
            if (tc.isPrimaryKey()) {
                sql.appendParam(new Param(tc.getValue(entity)));
            }
        }
        return sql;
    }

    public Sql parseInsertSql(Object entity) {
        if (entity.getClass() != clazz) {
            throw new RuntimeException("Parm is not the type of " + clazz.getName());
        }
        Sql sql = new Sql(null);
        sql.appendSql(this.insertSql);
        sql.setReturnType(void.class);
        for (TableColumn tc : tableColumnMap.values()) {
            sql.appendParam(new Param(tc.getValue(entity)));
        }
        return sql;
    }


	private String createColumnName(Field field) {
        Column column = ReflectionUtils.getAnnotation(field, Column.class);
        if (column == null || StringUtils.isEmpty(column.name())) {
			return nameHandler.convertToColumnName(field.getName());
		}
		return column.name();
	}

	@Override
	public void close() throws IOException {
        tableColumnMap.clear();
    }

}
