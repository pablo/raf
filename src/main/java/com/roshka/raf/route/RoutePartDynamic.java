package com.roshka.raf.route;

import java.util.Map;

public class RoutePartDynamic extends RoutePart {
	
	private String parameterName;

	public RoutePartDynamic(String parameterName)
	{
		this.parameterName = parameterName;
	}
	
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public boolean matches(String value, Map<String, String> routeParameters) {
		// since it's a dynamic value, we just match anything, but add it to routeParameters first
		routeParameters.put(parameterName, value);
		return true;
	}

}
