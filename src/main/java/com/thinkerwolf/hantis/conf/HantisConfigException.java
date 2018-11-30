package com.thinkerwolf.hantis.conf;

public class HantisConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971843246649637828L;

	public HantisConfigException(String message) {
		super(message);
	}

	public HantisConfigException(Throwable cause) {
		super(cause);
	}

	public HantisConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
