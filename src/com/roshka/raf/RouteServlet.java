package com.roshka.raf;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roshka.raf.context.RAFContext;
import com.roshka.raf.encoding.JSONEncoderHelper;
import com.roshka.raf.encoding.XMLEncoderHelper;
import com.roshka.raf.exception.RAFException;
import com.roshka.raf.params.ParametersProcessor;
import com.roshka.raf.route.RequestMethod;
import com.roshka.raf.route.Route;
import com.roshka.raf.route.RouteExecutorManager;
import com.roshka.raf.route.RouteManager;

/**
 * Servlet implementation class RouteServlet
 */
public class RouteServlet extends HttpServlet {
	
	enum SerializationType 
	{
		Xml,
		Json
	}
		
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RouteServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		RouteManager.initialize(config.getServletContext());
		RAFContext.initialize(config.getServletContext());
	}
	
	/**
	 * This is the common method that handles all request methods
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void processRoute(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		
		String pathInfo = req.getPathInfo();
		
		String extension = null;
		int lastIndex = pathInfo.lastIndexOf('.');
		if (lastIndex >= 0) {
			extension = pathInfo.substring(lastIndex);
			pathInfo = pathInfo.substring(0, lastIndex);
		}
		
		Route r = RouteManager.getRoute(pathInfo);
		
		
		SerializationType serializeIn = SerializationType.Json;
		Object oResponse = null;
		ServletContext sctx = getServletContext();
		RAFContext rctx = (RAFContext) sctx.getAttribute(RAFContext.RAF_CONTEXT_SERVLET_CONTEXT_KEY);
		if (extension == null || extension.equalsIgnoreCase(".json")) {
			// do nothing, default behaviour
		} else if (extension.equalsIgnoreCase(".xml")) {
			serializeIn = SerializationType.Xml;
		}
		
		try {
			if (r == null) {
				throw new RAFException(RAFException.ERRCODE_INVALID_ROUTE, String.format("Route [%s] does not exist", pathInfo)); 
			} else {
				// checks if route accepts method
				if (!r.acceptsMethod(RequestMethod.fromString(req.getMethod()))) {
					throw new RAFException(RAFException.ERRCODE_INVALID_METHOD, String.format("Route [%s] does not accept method [%s]", pathInfo, req.getMethod()));
				}
				
				// process query parameters
				ParametersProcessor pp = new ParametersProcessor(req, r);
				Object[] params;
					params = pp.getParameters();
					oResponse = RouteExecutorManager.executeRoute(rctx, r, params);
			}
		} catch (RAFException e) {
			oResponse = rctx.getGeneralError(e); 
		}
		
		switch(serializeIn)
		{
		case Json:
			JSONEncoderHelper.submitJSONResponse(req, resp, oResponse);
			break;
		case Xml:
			XMLEncoderHelper.submitXMLResponse(req, resp, oResponse);
			break;
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRoute(req, resp);
	}
	


}
