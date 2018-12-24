package com.thinkerwolf.hantis.cache;

public class SychronizedCache extends SimpleCache {

	@Override
	public synchronized void putObject(Object key, Object value) {
		super.putObject(key, value);
	}
	
	@Override
	public synchronized Object getObject(Object key) {
		return super.getObject(key);
	}

	@Override
	public synchronized Object removeObject(Object key) {
		return super.removeObject(key);
	}

	@Override
	public synchronized int getSize() {
		return super.getSize();
	}

	@Override
	public synchronized void clear() {
		super.clear();
	}

}
