package com.roshka.raf.route;

import java.lang.reflect.Field;
import java.util.List;

import com.roshka.raf.refl.RAFMethod;
import com.roshka.raf.refl.RAFParameter;

public class Route {
	
	private String name;
	private Class<?> actionClass;
	private RAFMethod actionMethod;
	private List<RAFParameter> parameters;
	private List<Field> contextFields;
	private List<Field> requestFields;
	private RequestMethod[] acceptedMethods;

	public Route(String name, Class<?> clazz, RAFMethod method, List<RAFParameter> parameters, List<Field> contextFields, List<Field> requestFields, RequestMethod[] acceptedMethods)
	{
		this.name = name;
		this.actionClass = clazz;
		this.actionMethod = method;
		this.parameters = parameters;
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

	public List<RAFParameter> getParameters() {
		return parameters;
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
