package com.roshka.raf.exception;

public class RAFException extends Exception {
	
	public static final String ERRCODE_UNEXPECTED_EXCEPTION = "raf000";
	public static final String ERRCODE_INVALID_PARAMETER_VALUE = "raf100";
	public static final String ERRCODE_INVALID_PARAMETER_DEFINITION = "raf110";
	public static final String ERRCODE_INVALID_ROUTE = "raf200";
	public static final String ERRCODE_INVALID_METHOD = "raf300";
	public static final String ERRCODE_UNREGISTERED_ENCODER = "raf400"; 
	

	private String code;
	private int httpStatus;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RAFException(int httpStatus, String code, String message) {
		this(httpStatus, code, message, null);
	}


	public RAFException(String code, String message) {
		this(0, code, message, null);
	}

	public RAFException(String code, String message, Throwable t) {
		this(0, code, message, t);
	}

	public String getCode() {
		return code;
	}

	public RAFException(int httpStatus, String code, String message, Throwable t) {
		super(message, t);
		this.code = code;
		this.httpStatus = httpStatus;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

}
