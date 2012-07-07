package com.roshka.raf.route;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.servlet.ServletContext;

import com.roshka.raf.annotations.RAFContext;
import com.roshka.raf.annotations.RAFMethod;
import com.roshka.raf.annotations.RAFRequest;
import com.roshka.raf.refl.RAFParameter;

public class RouteManager {
	
	private static boolean _initialized;
	private static Map<String, List<Route>> _routesMap;
	private static Set<String> _routesSet;
	private static List<Route> _routesList;
	private static Set<CtClass> _classesToProcess;

	public static void initialize(ServletContext ctx)
	{
		if (!_initialized)
			doInitialize(ctx);
	}
 
	private static void doInitialize(ServletContext ctx) {
		_routesMap = new HashMap<String, List<Route>>();
		_routesSet = new HashSet<String>();
		_routesList = new ArrayList<Route>();
		_classesToProcess = new HashSet<CtClass>();
		String rp = ctx.getRealPath("WEB-INF/classes/");
		String rpwl = ctx.getRealPath("WEB-INF/lib/");
		loadClasses(new File(rp));
		loadClassesLib(new File(rpwl));
		try {
			loadRoutes();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_classesToProcess.clear();
		_classesToProcess = null;
		_initialized = true;
	}
	
	public static Route getRoute(String path, Map<String, String> routeParameters)
	{
		
		if (path == null)
			return null;
		
		int i;
		
		String[] pathParts = path.split("/");
		
		String routeKey = null;
		for (i=0; i<pathParts.length; i++) {
			String routePart = pathParts[i];
			if (routePart.length() > 0) {
				routeKey = routePart;
				break;
			}
		}
		
		// calculate possible routes and return if a match is found
		List<Route> possibleRoutes = _routesMap.get(routeKey);
		
		if (possibleRoutes == null)
			// there are NO routes with this key
			return null;
		
		Route matchingRoute = null;
		for (Route r : possibleRoutes) 
		{
			if (r.matches(pathParts, routeParameters)) {
				matchingRoute = r;
				break;
			}
		}	
		
		return matchingRoute;
	}
	
	public static List<RouteInfo> getRoutesInfo()
	{
		List<RouteInfo> ret = new ArrayList<RouteInfo>();
		for (List<Route> routes : _routesMap.values()) {
			for (Route route : routes) {
				RouteInfo ri = new RouteInfo();
				ri.setRouteErrors(route.getRouteErrors());
				ri.setKey(route.getKey());
				ri.setName(route.getName());
				ri.setStatus(route.getStatus());
				ret.add(ri);
			}
		}
		return ret;
	}
	
	private static RAFParameter createRAFParameter(Class<?> clazz, com.roshka.raf.annotations.RAFParameter rpAnnotation)
	{
		RAFParameter ret = new RAFParameter(clazz, rpAnnotation.name());
		ret.setMandatory(rpAnnotation.mandatory());
		if (!rpAnnotation.defaultValue().equals("[unassigned]")) {
			ret.setDefaultValue(rpAnnotation.defaultValue());
		}
		ret.setDefaultValue(rpAnnotation.defaultValue());
		ret.setDateFormat(rpAnnotation.dateFormat());
		return ret;
	}
	
	private static void mapRoute(Route r)
	{
		// add to active routes list with that key
		List<Route> routes = _routesMap.get(r.getKey());
		if (routes == null) {
			routes = new ArrayList<Route>();
			_routesMap.put(r.getKey(), routes);
		}
		routes.add(r);
	}
	
	private static void loadRoute(Class<?> clazz, List<Field> contextFields, List<Field> requestFields, Method m, RAFMethod rafMethodAnnotation) 
	{
		Route r = new Route(rafMethodAnnotation, contextFields, requestFields);
		_routesList.add(r);
		
		if (_routesSet.contains(r.getName())) {
			// duplicated route
			r.addError(
					RouteError.ROUTE_ERROR_CODE_DUPLICATED_ROUTE, 
					String.format("Route [%s] is already registering. Skipping route for class [%s] and method [%s]", r.getName(), clazz.getName(), m.getName())
			);
			r.setStatus(Route.Status.RouteInvalid);
		} else {
			_routesSet.add(r.getName());
		}
		
		Annotation[][] annotations = m.getParameterAnnotations();
		List<RAFParameter> params = new ArrayList<RAFParameter>();
		for (int i = 0; i < annotations.length; i++) {
			Annotation[] paramAnnotations = annotations[i];
			Class<?> parameterClass = m.getParameterTypes()[i];
			com.roshka.raf.annotations.RAFParameter rpAnnotation = null;
			for (Annotation annotation : paramAnnotations) {
				if (annotation instanceof com.roshka.raf.annotations.RAFParameter) {
					rpAnnotation = (com.roshka.raf.annotations.RAFParameter) annotation;
					break;
				}
			}
			if (rpAnnotation != null)  {
				params.add(createRAFParameter(parameterClass, rpAnnotation));
			} else {
				r.addError(
						RouteError.ROUTE_ERROR_INVALID_ANNOTATION_PARAMETER, 
						String.format("Parameter #%d of method [%s] of route [%s] must have a RAFParameter annotation", i, m.getName(), r.getName())
				);
				r.setStatus(Route.Status.RouteInvalid);
			}
		}
		
		if (r.getStatus() == Route.Status.RouteBuilding) {
			r.setStatus(Route.Status.RouteActive);
			com.roshka.raf.refl.RAFMethod rafMethod = new com.roshka.raf.refl.RAFMethod(m, params);
			r.setActionMethod(rafMethod);
			r.setActionClass(clazz);
		}
		// activating route...
		mapRoute(r);
		
	}
	
	private static void loadRoutesFromClass(Class<?> clazz) 
	{
		
		Field[] fields = clazz.getDeclaredFields();
//		clazz.get
		List<Field> contextFields = new ArrayList<Field>();
		List<Field> requestFields = new ArrayList<Field>();
		for (Field field : fields) {
			RAFContext rc = field.getAnnotation(RAFContext.class);
			if (rc != null) {
				
				if (com.roshka.raf.context.RAFContext.class.isAssignableFrom(field.getType())) {
					contextFields.add(field);
				}
			}
			RAFRequest rr = field.getAnnotation(RAFRequest.class);
			if (rr != null) {
				if (field.getType().isAssignableFrom(com.roshka.raf.request.RAFRequest.class)) {
					requestFields.add(field);
				}
			}
		}
		
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			RAFMethod rm = m.getAnnotation(RAFMethod.class);
			if (rm != null) {
				loadRoute(clazz, contextFields, requestFields, m, rm);
			}
		}
		
	}
	
	private static void loadRoutes() 
		throws CannotCompileException
	{
		for (CtClass ctClass : _classesToProcess) {
			
			Class<?> clazz = ctClass.toClass();
			loadRoutesFromClass(clazz);
		}
	}
	
	private static void loadClassesLib(File directory)
	{
		// return if directory does not exist
        if (!directory.exists()) {
            return;
        }
        
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadClasses(file);
            } else if (file.getName().endsWith(".jar")) {
            	try {
            		
            		ZipFile zfile = new ZipFile(file);
            		
            		Enumeration<? extends ZipEntry> entries = zfile.entries();
            		
                	ClassPool cp = ClassPool.getDefault();
            		while (entries.hasMoreElements()) {
            			ZipEntry ze = entries.nextElement();
                		if (ze.getName().endsWith(".class")) {
                			// process class
                			CtClass ctClass = cp.makeClass(zfile.getInputStream(ze));
                        	CtMethod[] ctMethods = ctClass.getMethods();
                        	for (CtMethod ctMethod : ctMethods) {
                        		Object[] annotations = ctMethod.getAvailableAnnotations();
                        		for (Object annotation : annotations) {
                        			if (annotation instanceof RAFMethod) {
                        				_classesToProcess.add(ctClass);
                        				break;
                        				
                        			}
                        		}
                        	}
                		}
            		}
            		
            	} catch (Exception e) {
            		// TODO: improve exception handling
            		e.printStackTrace();
            	}
            	
            	
            }
        }
        
	}
	
	private static void loadClasses(File directory)
	{
		// return if directory does not exist
        if (!directory.exists()) {
            return;
        }
        
        // 
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadClasses(file);
            } else if (file.getName().endsWith(".class")) {
            	try {
                	DataInputStream dstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

                	ClassPool cp = ClassPool.getDefault();
                	CtClass ctClass = cp.makeClass(dstream);
                	
                	CtMethod[] ctMethods = ctClass.getMethods();
                	for (CtMethod ctMethod : ctMethods) {
                		Object[] annotations = ctMethod.getAvailableAnnotations();
                		for (Object annotation : annotations) {
                			if (annotation instanceof RAFMethod) {
                				_classesToProcess.add(ctClass);
                				break;
                				
                			}
                		}
                	}
                	
            	} catch (Exception e) {
            		// TODO: improve exception handling
            		e.printStackTrace();
            	}
            	
            	
            }
        }

	}
	
	public static void main(String[] args)
	{
		String route = "/asdf/p1/p2/p3";
		String[] routeParts = route.split("/");
		
		int i = 0;
		for (String part : routeParts) {
			System.out.println(String.format("a --> %02d: [%s]", i++, part));
		}
		
		route = "asdf/p1/p2/p3";
		routeParts = route.split("/");
		i = 0;
		for (String part : routeParts) {
			System.out.println(String.format("b --> %02d: [%s]", i++, part));
		}
		
		route = "/// ///";
		routeParts = route.split("/");
		i = 0;
		for (String part : routeParts) {
			System.out.println(String.format("c --> %02d: [%s] [%d]", i++, part, part.length()));
		}
		
		System.out.println("Salci, con jorge.");
	}
	
	
	
}
