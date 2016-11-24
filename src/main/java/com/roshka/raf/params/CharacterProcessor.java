package com.roshka.raf.params;

import com.roshka.raf.exception.RAFException;

public class CharacterProcessor {
	
	public static char parseCharacter(String parameterName, String value)
			throws RAFException
	{
		char ret;
		if (value.length() == 1) {
			ret = value.charAt(0);
		} else {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to character parameter %s. Its length must be one.", value, parameterName));
		}
		return ret;
	}

	

}
