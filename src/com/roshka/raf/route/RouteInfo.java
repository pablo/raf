package com.roshka.raf.route;

import java.util.List;

import com.roshka.raf.route.Route.Status;

public class RouteInfo {

	private String name;
	private String key;
	private Status status;
	private List<RouteError> routeErrors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<RouteError> getRouteErrors() {
		return routeErrors;
	}

	public void setRouteErrors(List<RouteError> routeErrors) {
		this.routeErrors = routeErrors;
	}
	
	
}
