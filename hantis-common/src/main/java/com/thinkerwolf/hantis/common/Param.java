package com.thinkerwolf.hantis.common;

import com.thinkerwolf.hantis.type.JDBCType;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Param other = (Param) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
