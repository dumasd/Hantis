package com.thinkerwolf.hantis;

import java.util.Properties;

import org.junit.Test;

import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.example.Blog;

public class UtilsTest {

	@Test
	public void propertyUtil() {

		Blog blog = new Blog();

		Properties props = new Properties();
		props.put("id", 1);
		props.setProperty("title", "google");
		props.setProperty("contentccc", "555");

		PropertyUtils.setProperties(blog, props);

		System.out.println(blog);
	}

}
