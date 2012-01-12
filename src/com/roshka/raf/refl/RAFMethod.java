package com.roshka.raf.refl;

import java.lang.reflect.Method;

public class RAFMethod {
	
	private Method method;

	public RAFMethod(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}
	
}
