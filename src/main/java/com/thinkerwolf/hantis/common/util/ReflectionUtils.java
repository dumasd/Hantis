package com.thinkerwolf.hantis.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils {
	/* Get methoh name cache */
	private static Map<String, String> getterMethodNameCache = new ConcurrentHashMap<>();
	/* Set method name cache */
	private static Map<String, String> setterMethodNameCache = new ConcurrentHashMap<>();
	/* Is method name cache */
	private static Map<String, String> isterMethodNameCache = new ConcurrentHashMap<>();

	/**
	 * Obtain Get method
	 *
	 * @param clazz
	 * @param propertyName
	 * @return
	 */
	public static Method getGetterMethod(final Class<?> clazz, final String propertyName) {
		if (clazz == null || propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("Target object is null");
		}
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		String getterName = getPropertyGetterName(propertyName);
		Method m = getMethod(clazz, getterName);
		if (m == null) {
			String isterName = getPropertyIsterName(propertyName);
			m = getMethod(clazz, isterName);
		}
		return m;
	}

	/**
	 * Obtain Set method
	 *
	 * @param clazz
	 * @param propertyName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getSetterMethod(final Class<?> clazz, final String propertyName, Class<?>... parameterTypes) {
		if (clazz == null || propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("Target object is null");
		}
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		String setterName = getPropertySetterName(propertyName);
		return getMethod(clazz, setterName, parameterTypes);
	}

	/**
	 * Obtain 'set' or 'get' method suffix
	 *
	 * @param propertyName
	 * @return
	 */
	public static String getPropertySetOrGetSuffix(String propertyName) {
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		char f = propertyName.charAt(0);
		return Character.toUpperCase(f) + propertyName.substring(1, propertyName.length());
	}

	/**
	 * Obtain property 'set' method name
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertySetterName(String propertyName) {
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		String name = setterMethodNameCache.get(propertyName);
		if (name == null) {
			name = "set" + getPropertySetOrGetSuffix(propertyName);
			setterMethodNameCache.put(propertyName, name);
		}
		return name;
	}

	/**
	 * Obtain property 'is' method name
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyIsterName(String propertyName) {
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		String name = isterMethodNameCache.get(propertyName);
		if (name == null) {
			name = "is" + getPropertySetOrGetSuffix(propertyName);
			isterMethodNameCache.put(propertyName, name);
		}
		return name;
	}

	/**
	 * Obtain property 'get' method name
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyGetterName(String propertyName) {
		if (propertyName == null || propertyName.length() == 0) {
			throw new IllegalArgumentException("PropertyName is empty");
		}
		String name = getterMethodNameCache.get(propertyName);
		if (name == null) {
			name = "get" + getPropertySetOrGetSuffix(propertyName);
			getterMethodNameCache.put(propertyName, name);
		}
		return name;
	}
	
	/**
	 * Obtain method by method's name
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class is null");
		}
		if (methodName == null || methodName.length() == 0) {
			throw new IllegalArgumentException("MethodName is empty");
		}
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Invoke method
	 * @param target
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object callMethod(Object target, Method method, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Obtain class annotation
	 *
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
		Class<?> c = clazz;
		while (c != null && c != Object.class) {
			T res = c.getAnnotation(annotationClass);
			if (res != null) {
				return res;
			} else {
				c = c.getSuperclass();
			}
		}
		return null;
	}

	/**
	 * Obtain field annotation
	 * 
	 * @param field
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

}
