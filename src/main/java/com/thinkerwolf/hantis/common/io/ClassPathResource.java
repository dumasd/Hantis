package com.thinkerwolf.hantis.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.thinkerwolf.hantis.common.util.ClassUtils;

public class ClassPathResource extends AbstractResource {
	private URL url;

	private ClassLoader classLoader;

	private String path;

	public ClassPathResource(String path) {
		this(path, (ClassLoader) null);
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		this.path = path;
		this.classLoader = ClassUtils.getDefaultClassLoader();
		if (this.classLoader != null) {
			this.url = classLoader.getResource(path);
		} else {
			this.url = ClassLoader.getSystemResource(path);
		}
	}

	@Override
	public boolean exists() {
		return url != null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

	@Override
	public String getPath() {
		return path;
	}

}
