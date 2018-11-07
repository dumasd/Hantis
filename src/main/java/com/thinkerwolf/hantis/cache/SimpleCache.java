package com.thinkerwolf.hantis.cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache implements Cache {

	private Map<Object, Object> cacheMap = new HashMap<>();

	@Override
	public void putObject(Object key, Object value) {
		cacheMap.put(key, value);
	}

	@Override
	public Object getObject(Object key) {
		return cacheMap.get(key);
	}

	@Override
	public Object removeObject(Object key) {
		return cacheMap.remove(key);
	}

	@Override
	public int getSize() {
		return cacheMap.size();
	}

	@Override
	public void clear() {
		for (Object obj : cacheMap.values()) {
			if (obj instanceof Closeable) {
				try {
					((Closeable) obj).close();
				} catch (IOException e) {
					// Ingore
				}
			}
		}
		cacheMap.clear();
	}

}
