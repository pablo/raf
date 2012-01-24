package com.roshka.raf.route;

import java.lang.reflect.Field;
import java.util.List;

import com.roshka.raf.refl.RAFMethod;

public class Route {

	// Basic Route Status & Information
	
	private String name; 						// route name. i.e.: '/some_action'  
	private RAFMethod actionMethod;				// meta information about method
	private Class<?> actionClass;				// class to instantiate to process this route

	private RequestMethod[] acceptedMethods;	// list of accepted HTTP methods for this route

	private List<Field> contextFields;			// annotated context fields
	private List<Field> requestFields;			// annotated request fields
	

	public Route(String name, Class<?> clazz, RAFMethod method, List<Field> contextFields, List<Field> requestFields, RequestMethod[] acceptedMethods)
	{
		this.name = name;
		this.actionClass = clazz;
		this.actionMethod = method;
		this.contextFields = contextFields;
		this.requestFields = requestFields;
		this.acceptedMethods = acceptedMethods;
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
	
}
