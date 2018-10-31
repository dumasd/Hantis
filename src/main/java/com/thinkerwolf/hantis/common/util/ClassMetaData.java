package com.thinkerwolf.hantis.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassMetaData {

	private Map<String, Field> fieldMap = new HashMap<>();

	private Map<String, Method> methodMap = new HashMap<>();

	public ClassMetaData(Class<?> clazz) {
		for (Field field : clazz.getFields()) {
			fieldMap.put(field.getName(), field);
		}
		for (Method method : clazz.getMethods()) {
			methodMap.put(method.getName(), method);
		}
	}

	public Method getMethod(String name) {
		return methodMap.get(name);
	}

	public Field getField(String name) {
		return fieldMap.get(name);
	}

}
