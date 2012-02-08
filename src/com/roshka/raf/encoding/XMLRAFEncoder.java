package com.roshka.raf.encoding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.roshka.raf.request.RAFRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XMLRAFEncoder extends BaseRAFEncoder {

	@Override
	public void submitEncodedResponse(RAFRequest rreq, Object o) throws IOException 
	{
		HttpServletResponse resp = rreq.getResponse();
		resp.setContentType("text/xml");
		resp.setCharacterEncoding("utf-8");
		DomDriver dd = new DomDriver("utf-8");
		XStream xs = new XStream(dd);
		xs.toXML(o, resp.getOutputStream());
	}

}
