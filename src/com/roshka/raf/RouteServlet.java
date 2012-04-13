package com.roshka.raf;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roshka.raf.context.RAFContext;
import com.roshka.raf.encoding.BaseRAFEncoder;
import com.roshka.raf.exception.RAFEncodingException;
import com.roshka.raf.exception.RAFException;
import com.roshka.raf.params.ParametersProcessor;
import com.roshka.raf.request.RAFRequest;
import com.roshka.raf.route.RequestMethod;
import com.roshka.raf.route.Route;
import com.roshka.raf.route.RouteExecutorManager;
import com.roshka.raf.route.RouteManager;

/**
 * Servlet implementation class RouteServlet
 */
public class RouteServlet extends HttpServlet {
	
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
		RAFContext.initialize(config);
	}
	
	private void printPanicError(HttpServletRequest req, HttpServletResponse resp, String panicMessage) throws IOException
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		PrintWriter pw = resp.getWriter();
		pw.write(panicMessage);
		pw.flush();
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
		
		if (pathInfo == null)
			pathInfo = "";
		
		String extension = null;
		int lastIndex = pathInfo.lastIndexOf('.');
		if (lastIndex >= 0) {
			if (lastIndex < pathInfo.length()) {
				extension = pathInfo.substring(lastIndex+1);
			}
			pathInfo = pathInfo.substring(0, lastIndex);
		}
		
		
		Map<String, String> routeParameters = new HashMap<String, String>();
		Route r = RouteManager.getRoute(pathInfo, routeParameters);
		
		Object oResponse = null;
		ServletContext sctx = getServletContext();
		
		RAFContext rctx = (RAFContext) sctx.getAttribute(RAFContext.RAF_CONTEXT_SERVLET_CONTEXT_KEY);
		RAFRequest rreq = new RAFRequest();
		rreq.setDefaultStatus(req.getMethod());
		rreq.setRequest(req);
		rreq.setResponse(resp);
		rreq.setRafContext(rctx);
		
		// TODO: change default depending on headers
		if (extension == null)
			extension = "json";
		BaseRAFEncoder encoder = rctx.getEncoder(extension);
		
		try {
			if (encoder == null) {
				encoder = rctx.getDefaultEncoder();
				// HTTP 400 
				throw new RAFException(HttpURLConnection.HTTP_BAD_REQUEST, RAFException.ERRCODE_UNREGISTERED_ENCODER, String.format("No encoder registered for extension [%s]", extension));
			} else if (r == null) {
				// HTTP 404 
				throw new RAFException(HttpURLConnection.HTTP_NOT_FOUND, RAFException.ERRCODE_INVALID_ROUTE, String.format("Route [%s] does not exist", pathInfo)); 
			} else {
				// checks if route accepts method
				if (!r.acceptsMethod(RequestMethod.fromString(req.getMethod()))) {
					// HTTP 405 
					throw new RAFException(HttpURLConnection.HTTP_BAD_METHOD, RAFException.ERRCODE_INVALID_METHOD, String.format("Route [%s] does not accept method [%s]", pathInfo, req.getMethod()));
				}
				
				// process query parameters
				ParametersProcessor pp = new ParametersProcessor(req, r, routeParameters);
				
				Object[] params;
					params = pp.getParameters();
					oResponse = RouteExecutorManager.executeRoute(rreq, r, params);
			}
		} catch (RAFException e) {
			if (e.getHttpStatus() > 0) {
				rreq.setHttpStatus(e.getHttpStatus());
			}
			oResponse = rctx.getGeneralError(e); 
		}

		// set status
		rreq.getResponse().setStatus(rreq.getHttpStatus());
		try {
			encoder.submitEncodedResponse(rreq, oResponse);
		} catch (RAFEncodingException e) {
			oResponse = rctx.getGeneralError(e);
			try {
				encoder.submitEncodedResponse(rreq, oResponse);
			} catch (RAFEncodingException ei) {
				printPanicError(req, resp, String.format("Can't encode encoding error: " + ei.getMessage()));
			}
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
