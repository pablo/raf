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

	public Route(String name, Class<?> clazz, RAFMethod method, List<RAFParameter> parameters, List<Field> contextFields)
	{
		this.name = name;
		this.actionClass = clazz;
		this.actionMethod = method;
		this.parameters = parameters;
		this.contextFields = contextFields;
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
	
}
