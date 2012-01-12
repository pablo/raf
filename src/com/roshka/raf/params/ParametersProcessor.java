package com.roshka.raf.params;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.roshka.raf.exception.RAFException;
import com.roshka.raf.refl.RAFParameter;
import com.roshka.raf.route.Route;

public class ParametersProcessor {

	private HttpServletRequest req;
	private Route route;
	
	public ParametersProcessor(HttpServletRequest req, Route route) {
		this.req = req;
		this.route = route;
	}
	
	private Object getNullOrZero(Class<?> clazz)
	{
		Object ret = null;
		if (clazz.isPrimitive()) {
			if (clazz.equals(Boolean.TYPE)) {
				ret = false;
			} else if (clazz.equals(Integer.TYPE)) {
				ret = 0;
			} //
		}
		return ret;
	}
	
	private Object getValue(Class<?> clazz, String parameterName, String value) 
		throws RAFException
	{
		Object ret = null;
		if (clazz.isPrimitive()) {
			if (clazz.equals(Boolean.TYPE)) {
				
			} else if (clazz.equals(Integer.TYPE)) {
				try {
					int v = Integer.parseInt(value);
					ret = v;
				} catch (NumberFormatException n) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to int parameter %s", value, parameterName));
				}
			}
			
			// todos
			
		} else {
			if (clazz.equals(String.class)) {
				ret = value;
			}
			
			// BigDecimal
			// BigInteger
			// Date
			
		}
		return ret;
	}
	
	public Object[] getParameters()
		throws RAFException
	{
		List<RAFParameter> rafParameters = route.getParameters();
		List<Object> objects = new ArrayList<Object>();
		for (RAFParameter rafParameter : rafParameters) {
			String paramValue = req.getParameter(rafParameter.getParameterName());
			
			// check mandatory parameter
			if (paramValue == null && rafParameter.isMandatory()) {
				throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Parameter %s is mandatory", rafParameter.getParameterName()));
			} else if (paramValue != null) {
				objects.add(getValue(rafParameter.getClazz(), rafParameter.getParameterName(), paramValue));
			} else {
				// add null (or default value parameter)
				if (rafParameter.getDefaultValue() != null) {
					objects.add(getValue(rafParameter.getClazz(), rafParameter.getParameterName(), rafParameter.getDefaultValue()));
				} else {
					objects.add(getNullOrZero(rafParameter.getClazz()));
				}
			}
		}
		return objects.toArray(new Object[0]);
	}
	
}
