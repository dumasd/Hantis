package com.thinkerwolf.hantis.transaction;

public class TransactionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5483155382670066237L;

	public TransactionException() {
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}
}
