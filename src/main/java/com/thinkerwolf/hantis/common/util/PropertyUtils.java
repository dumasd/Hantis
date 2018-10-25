package com.thinkerwolf.hantis.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertyUtils {

	public static void setProperties(Object target, Properties props) {
		for (Entry<Object, Object> entry : props.entrySet()) {
			String propertyName = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			setProperty(target, propertyName, value);
		}
	}

	public static void setProperty(Object target, String propertyName, Object propertyValue) {
		Class<?> clazz = target.getClass();
		try {
			Field f = clazz.getDeclaredField(propertyName);
			if (f != null) {
				f.setAccessible(true);
				f.set(target, propertyValue);
			}
		} catch (Exception e) {
			Method setterMethod = ReflectionUtils.getSetterMethod(target, propertyName, propertyValue.getClass());
			try {
				ReflectionUtils.callMethod(target, setterMethod, propertyValue);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
