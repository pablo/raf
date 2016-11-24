package com.roshka.raf.params;

import com.roshka.raf.exception.RAFException;

public class BooleanProcessor {
	
	public static boolean parseBoolean(String parameterName, String value)
			throws RAFException
	{
		boolean ret;
		ret = Boolean.parseBoolean(value);
		return ret;
	}
			

}
