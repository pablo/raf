package com.roshka.raf.refl;

import java.lang.reflect.Method;
import java.util.List;

public class RAFMethod {
	
	private Method method;
	private List<RAFParameter> parameters;	// method's parameter list

	public RAFMethod(Method method, List<RAFParameter> parameters) {
		this.method = method;
		this.parameters = parameters;
	}

	public Method getMethod() {
		return method;
	}
	
	public List<RAFParameter> getParameters() {
		return parameters;
	}
	
}
