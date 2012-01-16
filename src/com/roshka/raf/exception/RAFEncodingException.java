package com.roshka.raf.exception;

public class RAFEncodingException extends RAFException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RAFEncodingException(String code, String message) {
		super(code, message);
	}

	public RAFEncodingException(String code, String message, Throwable t) {
		super(code, message, t);
	}

}
