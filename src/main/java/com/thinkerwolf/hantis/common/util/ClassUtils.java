package com.thinkerwolf.hantis.common.util;

public class ClassUtils {

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ClassUtils.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
				}
			}
		}
		return cl;
	}

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

	public static <T> T newInstance(Class<T> clazz, Object... args) {
		if (args.length == 0) {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			Class<?>[] parameterTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				parameterTypes[i] = args[i].getClass();
			}
			try {
				return clazz.getConstructor(parameterTypes).newInstance(args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
