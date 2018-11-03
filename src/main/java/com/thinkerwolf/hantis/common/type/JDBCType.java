package com.thinkerwolf.hantis.common.type;

import java.sql.Types;

public enum JDBCType {
    /**
     * Identifies the generic SQL type {@code BIT}.
     */
    BIT(Types.BIT),
    /**
     * Identifies the generic SQL type {@code TINYINT}.
     */
    TINYINT(Types.TINYINT),
    /**
     * Identifies the generic SQL type {@code SMALLINT}.
     */
    SMALLINT(Types.SMALLINT),
    /**
     * Identifies the generic SQL type {@code INTEGER}.
     */
    INTEGER(Types.INTEGER),
    /**
     * Identifies the generic SQL type {@code BIGINT}.
     */
    BIGINT(Types.BIGINT),
    /**
     * Identifies the generic SQL type {@code FLOAT}.
     */
    FLOAT(Types.FLOAT),
    /**
     * Identifies the generic SQL type {@code REAL}.
     */
    REAL(Types.REAL),
    /**
     * Identifies the generic SQL type {@code DOUBLE}.
     */
    DOUBLE(Types.DOUBLE),
    /**
     * Identifies the generic SQL type {@code NUMERIC}.
     */
    NUMERIC(Types.NUMERIC),
    /**
     * Identifies the generic SQL type {@code DECIMAL}.
     */
    DECIMAL(Types.DECIMAL),
    /**
     * Identifies the generic SQL type {@code CHAR}.
     */
    CHAR(Types.CHAR),
    /**
     * Identifies the generic SQL type {@code VARCHAR}.
     */
    VARCHAR(Types.VARCHAR),
    /**
     * Identifies the generic SQL type {@code LONGVARCHAR}.
     */
    LONGVARCHAR(Types.LONGVARCHAR),
    /**
     * Identifies the generic SQL type {@code DATE}.
     */
    DATE(Types.DATE),
    /**
     * Identifies the generic SQL type {@code TIME}.
     */
    TIME(Types.TIME),
    /**
     * Identifies the generic SQL type {@code TIMESTAMP}.
     */
    TIMESTAMP(Types.TIMESTAMP),
    /**
     * Identifies the generic SQL type {@code BINARY}.
     */
    BINARY(Types.BINARY),
    /**
     * Identifies the generic SQL type {@code VARBINARY}.
     */
    VARBINARY(Types.VARBINARY),
    /**
     * Identifies the generic SQL type {@code LONGVARBINARY}.
     */
    LONGVARBINARY(Types.LONGVARBINARY),
    /**
     * Identifies the generic SQL value {@code NULL}.
     */
    NULL(Types.NULL),
    /**
     * Indicates that the SQL type is database-specific and gets mapped to a
     * Java object that can be accessed via the methods getObject and setObject.
     */
    OTHER(Types.OTHER),
    /**
     * Indicates that the SQL type is database-specific and gets mapped to a
     * Java object that can be accessed via the methods getObject and setObject.
     */
    JAVA_OBJECT(Types.JAVA_OBJECT),
    /**
     * Identifies the generic SQL type {@code DISTINCT}.
     */
    DISTINCT(Types.DISTINCT),
    /**
     * Identifies the generic SQL type {@code STRUCT}.
     */
    STRUCT(Types.STRUCT),
    /**
     * Identifies the generic SQL type {@code ARRAY}.
     */
    ARRAY(Types.ARRAY),
    /**
     * Identifies the generic SQL type {@code BLOB}.
     */
    BLOB(Types.BLOB),
    /**
     * Identifies the generic SQL type {@code CLOB}.
     */
    CLOB(Types.CLOB),
    /**
     * Identifies the generic SQL type {@code REF}.
     */
    REF(Types.REF),
    /**
     * Identifies the generic SQL type {@code DATALINK}.
     */
    DATALINK(Types.DATALINK),
    /**
     * Identifies the generic SQL type {@code BOOLEAN}.
     */
    BOOLEAN(Types.BOOLEAN),

	/* JDBC 4.0 Types */

    /**
     * Identifies the SQL type {@code ROWID}.
     */
    ROWID(Types.ROWID),
    /**
     * Identifies the generic SQL type {@code NCHAR}.
     */
    NCHAR(Types.NCHAR),
    /**
     * Identifies the generic SQL type {@code NVARCHAR}.
     */
    NVARCHAR(Types.NVARCHAR),
    /**
     * Identifies the generic SQL type {@code LONGNVARCHAR}.
     */
    LONGNVARCHAR(Types.LONGNVARCHAR),
    /**
     * Identifies the generic SQL type {@code NCLOB}.
     */
    NCLOB(Types.NCLOB),
    /**
     * Identifies the generic SQL type {@code SQLXML}.
     */
    SQLXML(Types.SQLXML),

    UNKONWN(9999),;
    private Integer type;

    JDBCType(final Integer type) {
        this.type = type;
    }

    public static JDBCType valueOf(int type) {
        for (JDBCType sqlType : JDBCType.class.getEnumConstants()) {
            if (type == sqlType.type)
                return sqlType;
        }
        throw new IllegalArgumentException("Type:" + type + " is not a valid " + "Types.java value.");
    }

    public static JDBCType getJDBCType(Class<?> classType) {
        JDBCType type = JDBCType.JAVA_OBJECT;


        return type;
    }

    public Integer getType() {
        return type;
    }


}
