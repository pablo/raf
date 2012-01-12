package com.roshka.raf.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import com.roshka.raf.beans.GeneralResponse;
import com.roshka.raf.exception.RAFException;

public class RAFContext {
	
	public static final String RAF_CONTEXT_SERVLET_CONTEXT_KEY = "raf_ctx.rshk";
	
	private Map<String, Object> attributes;
	private static boolean _initialized;
	
	public static void initialize(ServletContext ctx)
	{
		if (!_initialized)
			doInitialize(ctx);
	}
	
	private static void doInitialize(ServletContext ctx)
	{
		String contextClassName = ctx.getInitParameter("raf_ctx_class");
		RAFContext rc = null;
		if (contextClassName != null) {
			try {
				Class<RAFContext> contextClass = (Class<RAFContext>) Class.forName(contextClassName);
				rc = contextClass.newInstance();
			} catch (Throwable e) {
				rc = new RAFContext();
			}
		} else {
			rc = new RAFContext();
		}
		rc.initialize();
		ctx.setAttribute(RAF_CONTEXT_SERVLET_CONTEXT_KEY, rc);
		_initialized = true;
	}

	public RAFContext()
	{
		attributes = new ConcurrentHashMap<String, Object>();
	}
	
	public void initialize()
	{
		//
		
		
	}
	
	public Object getAttribute(String key)
	{
		return attributes.get(key);
	}
	
	public void setAttribute(String key, Object value) 
	{
		attributes.put(key, value);
	}
	
	public Object getGeneralError(Throwable t)
	{
		GeneralResponse gr = null;
		if (t instanceof RAFException) {
			RAFException re = (RAFException)t;
			gr = new GeneralResponse("ERR", re.getCode(), re.getMessage());
		} else {
			gr = new GeneralResponse("ERR", RAFException.ERRCODE_UNEXPECTED_EXCEPTION, "Unexpected Error: " + t.getMessage());
		}
		return gr;
	}
	
}
