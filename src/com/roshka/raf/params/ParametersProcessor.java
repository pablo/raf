package com.roshka.raf.params;

import java.math.BigDecimal;
import java.math.BigInteger;
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
			if (clazz.equals(Byte.TYPE)) {
				ret = (byte)0;
			} else if (clazz.equals(Short.TYPE)) {
				ret = (short)0;
			} else if (clazz.equals(Integer.TYPE)) {
				ret = 0;
			} else if (clazz.equals(Long.TYPE)) {
				ret = 0L;
			} else if (clazz.equals(Float.TYPE)) {
				ret = 0.0f;
			} else if (clazz.equals(Double.TYPE)) {
				ret = 0.0;
			} else if (clazz.equals(Boolean.TYPE)) {
				ret = false;
			} else if (clazz.equals(Character.TYPE)) {
				ret = '\0';
			}
		} else {
			ret = null;
		}
		return ret;
	}
	
	private Object getValue(Class<?> clazz, String parameterName, String value) 
		throws RAFException
	{
		Object ret = null;
		if (clazz.isPrimitive()) {
			
			if (clazz.equals(Byte.TYPE)) {
				try {
					ret = Byte.parseByte(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to byte parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Short.TYPE)) {
				try {
					ret = Short.parseShort(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to short parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Integer.TYPE)) {
				try {
					ret = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to int parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Long.TYPE)) {
				try {
					ret = Long.parseLong(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to long parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Float.TYPE)) {
				try {
					ret = Float.parseFloat(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to float parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Double.TYPE)) {
				try {
					ret = Double.parseDouble(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to double parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Boolean.TYPE)) {
				try {
					ret = Boolean.parseBoolean(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to boolean parameter %s", value, parameterName));
				}
			} else if (clazz.equals(Character.TYPE)) {
				if (value.length() == 1) {
					ret = value.charAt(0);
				} else {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to character parameter %s. Its length must be one.", value, parameterName));
				}
			}
			
		} else {
			if (clazz.equals(String.class)) {
				ret = value;
			} else if (clazz.equals(BigDecimal.class)) {
				try {
					ret = new BigDecimal(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to BigDecimal parameter %s.", value, parameterName));
				}
			} else if (clazz.equals(BigInteger.class)) {
				try {
					ret = new BigInteger(value);
				} catch (NumberFormatException e) {
					throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER, String.format("Value [%s] can't be converted to BigInteger parameter %s.", value, parameterName));
				}
			}
			
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
