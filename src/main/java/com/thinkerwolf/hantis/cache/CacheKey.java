package com.thinkerwolf.hantis.cache;

/**
 * 缓存键值
 * 
 * @author wukai
 *
 */
public class CacheKey {

	private static final int PRIME = 37;
	private static final int INITIAL_VALUE = 17;

	private int iPrime;
	private int hashCode;

	public CacheKey() {
		this.iPrime = PRIME;
		this.hashCode = INITIAL_VALUE;
	}

	public CacheKey append(Object obj) {
		if (obj == null) {
			hashCode *= iPrime;
		} else {
			hashCode = hashCode * iPrime + obj.hashCode();
		}
		return this;
	}

	public CacheKey append(byte[] arrays) {
		for (byte array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(short[] arrays) {
		for (short array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(int[] arrays) {
		for (int array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(long[] arrays) {
		for (long array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(float[] arrays) {
		for (float array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(double[] arrays) {
		for (double array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(boolean[] arrays) {
		for (boolean array : arrays) {
			append(array);
		}
		return this;
	}

	public CacheKey append(Object[] objs) {
		if (objs == null) {
			hashCode *= iPrime;
		} else {
			for (Object obj : objs) {
				append(obj);
			}
		}
		return this;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return "CacheKey [hashCode:" + hashCode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this.getClass() != obj.getClass()) {
			return false;
		}
		CacheKey ck = (CacheKey) obj;
		if (hashCode == ck.hashCode) {
			return true;
		}
		return false;
	}
	// public static void main(String[] args) {
	// CacheKey k = new CacheKey();
	// }
}
