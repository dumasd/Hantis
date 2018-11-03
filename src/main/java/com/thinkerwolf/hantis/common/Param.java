package com.thinkerwolf.hantis.common;

import com.thinkerwolf.hantis.common.type.JDBCType;

public final class Param {

    private JDBCType type;

    private Object value;

    public Param(JDBCType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Param(Object value) {
        this.type = JDBCType.UNKONWN;
        this.value = value;
    }

    public JDBCType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type.name() + ":" + value;
    }
}
