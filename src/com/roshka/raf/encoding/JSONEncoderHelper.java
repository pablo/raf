package com.roshka.raf.encoding;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class JSONEncoderHelper {
	
	public static void submitJSONResponse(HttpServletRequest req, HttpServletResponse resp, Object o) 
			throws IOException
		{
			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");
			PrintWriter pw = resp.getWriter();
			Gson gson = new Gson();
			gson.toJson(o, pw);
		}
		

}
