package com.roshka.raf.params;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.roshka.raf.exception.RAFException;

public class NumberProcessor {

	public static byte parseByte(String parameterName, String value)
		throws RAFException
	{
		byte ret;
		try {
			ret = Byte.parseByte(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to byte parameter %s", value, parameterName));
		}
		return ret;
	}
	
	public static short parseShort(String parameterName, String value)
			throws RAFException
	{
		short ret;
		try {
			ret = Short.parseShort(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to short parameter %s", value, parameterName));
		}
		return ret;
	}
	
	public static int parseInt(String parameterName, String value)
			throws RAFException
	{
		int ret;
		try {
			ret = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to int parameter %s", value, parameterName));
		}
		return ret;
	}
	
	public static long parseLong(String parameterName, String value)
			throws RAFException
	{
		long ret;
		try {
			ret = Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to long parameter %s", value, parameterName));
		}
		return ret;
	}
			
	public static float parseFloat(String parameterName, String value)
			throws RAFException
	{
		float ret;
		try {
			ret = Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to float parameter %s", value, parameterName));
		}
		return ret;
	}
			
	public static double parseDouble(String parameterName, String value)
			throws RAFException
	{
		double ret;
		try {
			ret = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to double parameter %s", value, parameterName));
		}
		return ret;
	}
	
	public static BigDecimal parseBigDecimal(String parameterName, String value)
		throws RAFException
	{
		BigDecimal ret = null;
		try {
			ret = new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to BigDecimal parameter %s.", value, parameterName));
		}
		return ret;
	}
			
	public static BigInteger parseBigInteger(String parameterName, String value)
			throws RAFException
	{
		BigInteger ret = null;
		try {
			ret = new BigInteger(value);
		} catch (NumberFormatException e) {
			throw new RAFException(RAFException.ERRCODE_INVALID_PARAMETER_VALUE, String.format("Value [%s] can't be converted to BigInteger parameter %s.", value, parameterName));
		}
		return ret;
	}
				
}
