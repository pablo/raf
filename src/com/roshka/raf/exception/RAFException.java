package com.roshka.raf.exception;

public class RAFException extends Exception {
	
	public static final String ERRCODE_UNEXPECTED_EXCEPTION = "raf000";
	public static final String ERRCODE_INVALID_PARAMETER_VALUE = "raf100";
	public static final String ERRCODE_INVALID_PARAMETER_DEFINITION = "raf110";
	public static final String ERRCODE_INVALID_ROUTE = "raf200";
	public static final String ERRCODE_INVALID_METHOD = "raf300";
	public static final String ERRCODE_UNREGISTERED_ENCODER = "raf400"; 
	

	private String code;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RAFException(String code, String message) {
		super(message);
		this.code = code;
	}

	public RAFException(String code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
