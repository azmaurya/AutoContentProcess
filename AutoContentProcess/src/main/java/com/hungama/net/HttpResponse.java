package com.hungama.net;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class HttpResponse implements AutoCloseable {
	final int statusCode;
	final String status;
	final Map<String, String[]> headers = new TreeMap<String, String[]>();
	//private static Logger slf4jLog = LoggerFactory.getLogger(HttpResponse.class);
	
	protected HttpEntity entity;
	
	protected HttpResponse(final int statusCode, final String status, final HttpEntity entity) {
		this.statusCode = statusCode;
		this.status = status;
		this.entity = entity;
	}
	
	protected void setHeader(final String name, final String value) {
		String[] values = headers.get(name);
		if(values == null) {
			headers.put(name, new String[] {value});
		} else {
			values = Arrays.copyOf(values, values.length+1);
			values[values.length - 1] = value;
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatus() {
		return status;
	}
	
	public String[] getHeader(final String name) {
		return headers.get(name);
	}

	public byte[] getResponse() throws IOException {
		if(entity == null) return null;
		return EntityUtils.toByteArray(entity);		
	}
	
	public InputStream getResponseAsStream() throws IOException {
		if(entity == null) return null;
		return entity.getContent();		
	}
	
	public String getResponseAsString() throws IOException {
		if(entity == null) return null;
		return EntityUtils.toString(entity, "UTF-8");				
	}
	
	public void writeResponseTo(final OutputStream os) throws IOException {
		if(entity != null) {
			entity.writeTo(os);
		}
	}
	
	public void close() throws IOException {
		if(entity != null) {
			//entity.consumeContent();
			EntityUtils.consume(entity);
			entity = null;
		}
	}
	
	public Map<String, String[]> getHeaders() {
		return headers;
	}

	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
}
