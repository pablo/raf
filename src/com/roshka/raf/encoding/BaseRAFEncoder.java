package com.roshka.raf.encoding;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roshka.raf.exception.RAFEncodingException;

public abstract class BaseRAFEncoder {
	public abstract void submitEncodedResponse(HttpServletRequest req, HttpServletResponse resp, Object o)
		throws IOException, RAFEncodingException;
}
