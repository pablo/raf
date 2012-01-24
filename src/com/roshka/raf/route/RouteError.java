package com.roshka.raf.route;

public class RouteError {
	
	public static final String ROUTE_ERROR_CODE_DUPLICATED_ROUTE = "r_100";
	public static final String ROUTE_ERROR_INVALID_ANNOTATION_PARAMETER = "r_200";
	

	private String code;
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
