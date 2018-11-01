package com.thinkerwolf.hantis.common.util;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.thinkerwolf.hantis.common.io.Resources;

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

	/**
	 * 扫描Class文件
	 * 
	 * @param basePackage
	 *            包名，可以包含通配符
	 * @return
	 */
	public static Set<Class<?>> scanClasses(String basePackage) {
		basePackage = basePackage.replaceAll("\\.", "/").replace(File.separatorChar, '/');
		String rootDir = Resources.getRootDir(basePackage);
		Pattern p = Pattern.compile(basePackage.replaceAll("\\*", ".*"));
		Set<String> set = ResourceUtils.findClasspathFilePaths(rootDir, "class");
		Set<Class<?>> result = new HashSet<>();
		for (Iterator<String> iter = set.iterator(); iter.hasNext();) {
			String s = iter.next();
			if (p.matcher(s).matches()) {
				String classname = s.replaceAll("/", ".").replaceAll(".class", "");
				result.add(forName(classname));
			}
		}
		return result;
	}

}
