package com.roshka.raf.route;

import java.lang.reflect.Field;
import java.util.List;

import com.roshka.raf.context.RAFContext;
import com.roshka.raf.exception.RAFException;

public class RouteExecutorManager {

	public static Object executeRoute(RAFContext rc, Route r, Object... params)
		throws RAFException
	{
		Object _instance;
		try {
			_instance = r.getActionClass().newInstance();
			List<Field> contextFields = r.getContextFields();
			for (Field field : contextFields) {
				field.set(_instance, rc);
			}
			return r.getActionMethod().getMethod().invoke(_instance, params);
		} catch (Throwable e) {
			throw new RAFException(RAFException.ERRCODE_UNEXPECTED_EXCEPTION, "Unexpected Exception: " + e.getMessage());
		} 
	}
	
}
