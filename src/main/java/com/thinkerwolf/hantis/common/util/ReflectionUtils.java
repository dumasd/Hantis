package com.thinkerwolf.hantis.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

	public static Class<?> forName(String name) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	public static Method getGetterMethod(Object target, String propertyName) {
		if (target == null || propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("Target object is null");
		}
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		char f = propertyName.charAt(0);
		String suffix = Character.toUpperCase(f) + propertyName.substring(1, propertyName.length());
		try {
			return target.getClass().getMethod("get" + suffix);
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				target.getClass().getMethod("is" + suffix);
			} catch (NoSuchMethodException | SecurityException e1) {
			}
		}
		return null;
	}

	public static Method getSetterMethod(Object target, String propertyName, Class<?>... parameterTypes) {
		if (target == null || propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("Target object is null");
		}
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		char f = propertyName.charAt(0);
		String suffix = Character.toUpperCase(f) + propertyName.substring(1, propertyName.length());
		try {
			return target.getClass().getMethod("set" + suffix, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return null;
	}

	public static Object callMethod(Object target, Method method, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(target, args);
	}

}
