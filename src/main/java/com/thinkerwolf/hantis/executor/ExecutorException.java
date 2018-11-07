package com.thinkerwolf.hantis.executor;

public class ExecutorException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4523751950182752825L;

	public ExecutorException(String message) {
        super(message);
    }

    public ExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorException(Throwable cause) {
        super(cause);
    }
}
