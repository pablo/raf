package com.roshka.raf.refl;

public class RAFParameter {
	
	private Class<?> clazz;
	private String parameterName;
	private String defaultValue;
	private boolean mandatory;
	
	public RAFParameter(Class<?> parameterClass, String parameterName)
	{
		this.clazz = parameterClass;
		this.parameterName = parameterName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getParameterName() {
		return parameterName;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
