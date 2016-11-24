package com.roshka.raf.route;

import java.util.Map;

public class RoutePartStatic extends RoutePart {
	
	private String value;
	
	public RoutePartStatic(String value)
	{
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean matches(String value, Map<String, String> routeParameters) {
		return getValue().equals(value);
	}
	
	

}
