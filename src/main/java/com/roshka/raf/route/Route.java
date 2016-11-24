package com.roshka.raf.route;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.roshka.raf.refl.RAFMethod;

public class Route {
	
	public enum Status
	{
		RouteBuilding,
		RouteActive,
		RouteInactive,
		RouteInvalid
	}

	// Basic Route Status & Information
	
	private String name; 						// route name. i.e.: '/some_action/{some_parameter}'
	
	private String key;							// route key, to use in hash map for all routes of this type
	
	private List<RoutePart> parts;				// list of all route parts
	
	private RAFMethod actionMethod;				// meta information about method
	private Class<?> actionClass;				// class to instantiate to process this route

	private RequestMethod[] acceptedMethods;	// list of accepted HTTP methods for this route

	private List<Field> contextFields;			// annotated context fields
	private List<Field> requestFields;			// annotated request fields
	
	private Status status;						// route status
	// possible values:
	// 0. RouteBuilding: means the route is being initialized
	// 1. RouteActive: means the route is active and is valid. ready to receive requests.
	// 2. RouteInactive: means the route is inactive. probably paused indefinitely by administrator.
	// 3. RouteInvalid: means the route is misconfigured. Could not be initialized in the first place.
	
	private List<RouteError> routeErrors;
	

	public Route(com.roshka.raf.annotations.RAFMethod rafMethodAnnotation, List<Field> contextFields, List<Field> requestFields)
	{
		this.name = rafMethodAnnotation.value();
		this.parts = new ArrayList<RoutePart>();
		
		parseParts();
		
		this.acceptedMethods = rafMethodAnnotation.acceptedMethods();
		
		// we start with a RouteBuilding status. It must change at some point
		this.status = Status.RouteBuilding;
		this.routeErrors = new ArrayList<RouteError>();
		this.contextFields = contextFields;
		this.requestFields = requestFields;
		
		/*
		this.actionClass = clazz;
		this.actionMethod = method;
		*/
	}
	
	private void parseParts() {
		String[] partsStrings = name.split("/");
		
		for (String partString : partsStrings)
		{
			// if route is "/something/else" then first string on split will be of length 0
			if (partString.length() == 0) continue;
			if (key == null)
				key = partString;
			RoutePart rp = null;
			if (partString.startsWith("{") && partString.endsWith("}")) {
				rp = new RoutePartDynamic(partString.substring(1, partString.length()-1));  
			} else {
				rp = new RoutePartStatic(partString);
			}
			parts.add(rp);
		}
		
	}
	
	public boolean matches(String[] pathParts, Map<String, String> routeParameters)
	{
		boolean ret = true;
		int matchedCount = 0;
		for (String pathPart : pathParts) {
			if (pathPart.length() == 0) continue;
			if (matchedCount < parts.size()) {
				RoutePart rp = parts.get(matchedCount);
				if (rp.matches(pathPart, routeParameters)) {
					matchedCount++;
				} else {
					ret = false;
					break;
				}
			} else {
				ret = false;
				break;
			}
		}
		if (!ret)
			// get ready for the next route
			routeParameters.clear();
		else
			ret = matchedCount == parts.size();
		return ret;
	}

	public String getName() {
		return name;
	}

	public Class<?> getActionClass() {
		return actionClass;
	}

	public RAFMethod getActionMethod() {
		return actionMethod;
	}

	public List<Field> getContextFields() {
		return contextFields;
	}

	public RequestMethod[] getAcceptedMethods() {
		return acceptedMethods;
	}

	public void setAcceptedMethods(RequestMethod[] acceptedMethods) {
		this.acceptedMethods = acceptedMethods;
	}
	
	public boolean acceptsMethod(RequestMethod requestMethod)
	{
		boolean ret = false;
		for (RequestMethod rm : acceptedMethods) {
			if (rm == requestMethod) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public List<Field> getRequestFields() {
		return requestFields;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void addError(String code, String description)
	{
		RouteError re = new RouteError();
		re.setCode(code);
		re.setDescription(description);
		this.addError(re);
	}
	
	public void addError(RouteError re)
	{
		routeErrors.add(re);
	}

	public List<RouteError> getRouteErrors() {
		return routeErrors;
	}

	public void setActionClass(Class<?> actionClass) {
		this.actionClass = actionClass;
	}

	public void setActionMethod(RAFMethod actionMethod) {
		this.actionMethod = actionMethod;
	}

	public List<RoutePart> getParts() {
		return parts;
	}

	public String getKey() {
		return key;
	}
	
}
