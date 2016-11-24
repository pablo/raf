package com.roshka.raf.route;

import java.util.Map;

public abstract class RoutePart {

	public abstract boolean matches(String value, Map<String, String> routeParameters);
	
}
