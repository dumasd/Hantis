package com.thinkerwolf.hantis;

import freemarker.template.*;

import org.junit.Test;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;

@SuppressWarnings("unused")
public class FreemarkerTest {

	@Test
	public void freemarker() throws Throwable {
		Class<?>[] classTypes = new Class<?>[] { 
				//Object.class,
				
				//Boolean.class, 
				//Byte.class, 
				//Short.class, 
				//Integer.class, 
				//Long.class, 
				//Float.class, 
				//Double.class,
				//BigDecimal.class,
				//String.class,
				//Timestamp.class,
				//Time.class,
				//Blob.class,
				//Clob.class,
				//byte[].class
				Character.class
		};
		// Class<?>[] classTypes = new Class<?>[] { int[].class };
		String fileName = "src/main/java/com/thinkerwolf/hantis/common/type/{%type%}TypeHandler.java";
		String templateLoadingDictory = "src/main/java/com/thinkerwolf/hantis/";
		String templateName = "typeHandler.txt";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setDirectoryForTemplateLoading(new File(templateLoadingDictory));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Template temp = cfg.getTemplate(templateName);
		Map<String, Object> root = new HashMap<>();
		for (Class<?> type : classTypes) {
			root.put("packageName", "com.thinkerwolf.hantis.common.type");
			Class<?> classType = type;
			String suffix;
			if (type.isArray()) {
				classType = type.getComponentType();
				String simpleName = classType.getSimpleName();
				suffix = Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1) + 's';
			} else {
				suffix = classType.getSimpleName();
			}
			root.put("type", suffix);
			if (classType.getName().startsWith("java.lang") || !classType.getName().contains(".")) {
				root.put("isNeedImport", false);
			} else {
				root.put("importClass", classType.getName());
				root.put("isNeedImport", true);
			}
			root.put("realType", type.getSimpleName());

			Writer out = new OutputStreamWriter(new FileOutputStream(fileName.replaceAll("\\{%type%\\}", suffix)));
			// Writer out = new OutputStreamWriter(System.out);
			temp.process(root, out);
		}

	}

}
