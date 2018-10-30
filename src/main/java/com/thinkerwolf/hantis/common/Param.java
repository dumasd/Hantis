package com.thinkerwolf.hantis.common;

public final class Param {

	private JDBCType type;

	private Object value;

	public Param(JDBCType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Param(Object value) {
		this.type = JDBCType.JAVA_OBJECT;
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
