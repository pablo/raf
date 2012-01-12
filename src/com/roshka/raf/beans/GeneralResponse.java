package com.roshka.raf.beans;

public class GeneralResponse {
	
	private String status;
	private Err error;
	
	public GeneralResponse()
	{
		this.status = "OK";
	}
	
	public GeneralResponse(String status, String errorCode, String errorDescription)
	{
		this.status = status;
		this.error = new Err();
		this.error.setCode(errorCode);
		this.error.setDescription(errorDescription);
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Err getError() {
		return error;
	}
	public void setError(Err error) {
		this.error = error;
	}
	
}
