package com.roshka.raf;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.roshka.raf.context.RAFContext;
import com.roshka.raf.exception.RAFException;
import com.roshka.raf.params.ParametersProcessor;
import com.roshka.raf.route.Route;
import com.roshka.raf.route.RouteExecutorManager;
import com.roshka.raf.route.RouteManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	
	private void sendJSONResponse(HttpServletRequest req, HttpServletResponse resp, Object o) 
		throws IOException
	{
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		PrintWriter pw = resp.getWriter();
		Gson gson = new Gson();
		gson.toJson(o, pw);
	}
	
	private void sendXMLResponse(HttpServletRequest req, HttpServletResponse resp, Object o) throws IOException
	{
		resp.setContentType("text/xml");
		resp.setCharacterEncoding("utf-8");
		DomDriver dd = new DomDriver("utf-8");
		XStream xs = new XStream(dd);
		xs.toXML(o, resp.getOutputStream());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
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
			sendJSONResponse(req, resp, oResponse);
			break;
		case Xml:
			sendXMLResponse(req, resp, oResponse);
			break;
		}
		
	}


}
