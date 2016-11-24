package com.roshka.raf.route;

public enum RequestMethod {
	HttpGet("GET"),
	HttpPost("POST"),
	HttpHead("HEAD"),
	HttpPut("PUT"),
	HttpDelete("DELETE"),
	HttpOption("OPTION"),
	HttpInvalid("Invalid Http Method");
	
	private String methodName;
	
	RequestMethod(String methodName)
	{
		this.methodName = methodName;
	}
	
	public String getMethodName()
	{
		return this.methodName;
	}
	
	public static RequestMethod fromString(String methodName)
	{
		if (methodName.equalsIgnoreCase(HttpGet.getMethodName())) {
			return HttpGet;
		} else if (methodName.equalsIgnoreCase(HttpPost.getMethodName())) {
			return HttpPost;
		} else if (methodName.equalsIgnoreCase(HttpHead.getMethodName())) {
			return HttpHead;
		} else if (methodName.equalsIgnoreCase(HttpPut.getMethodName())) {
			return HttpPut;
		} else if (methodName.equalsIgnoreCase(HttpDelete.getMethodName())) {
			return HttpDelete;
		} else if (methodName.equalsIgnoreCase(HttpOption.getMethodName())) {
			return HttpOption;
		}
		return HttpInvalid;
	}
	
	
	
}
