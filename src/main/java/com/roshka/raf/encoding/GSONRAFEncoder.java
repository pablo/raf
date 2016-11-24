package com.roshka.raf.encoding;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.roshka.raf.request.RAFRequest;

public class GSONRAFEncoder extends BaseRAFEncoder {

	@Override
	public void submitEncodedResponse(RAFRequest rreq, Object o) throws IOException 
	{
		HttpServletResponse resp = rreq.getResponse();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		PrintWriter pw = resp.getWriter();
		Gson gson = new Gson();
		gson.toJson(o, pw);
	}

}
