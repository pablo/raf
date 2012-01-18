package com.roshka.raf.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roshka.raf.context.RAFContext;

public class RAFRequest {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private RAFContext rafContext;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public RAFContext getRafContext() {
		return rafContext;
	}
	public void setRafContext(RAFContext rafContext) {
		this.rafContext = rafContext;
	}
	
	
	
	
}
