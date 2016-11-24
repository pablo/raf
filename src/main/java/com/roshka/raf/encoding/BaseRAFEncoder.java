package com.roshka.raf.encoding;

import java.io.IOException;

import com.roshka.raf.exception.RAFEncodingException;
import com.roshka.raf.request.RAFRequest;

public abstract class BaseRAFEncoder {
	public abstract void submitEncodedResponse(RAFRequest rreq, Object o)
		throws IOException, RAFEncodingException;
}
