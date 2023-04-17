package com.hungama.net;


import java.io.File;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
/*import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;*/




public class Http {

//	private static Logger slf4jLog = Logger.getLogger(Http.class);
	protected final HttpClient client;
	final static String USER_AGENT = "Mozilla/5.0 HungamaHttpClient/1.0";
	public final static String HEADER_CONTENT_TYPE = "Content-Type";
	public final static String HEADER_LOCATION = "Location";
	static String salt = "zz8Vs3o8Y2B5z041Z44zydE";
   
    static long window = 20000;

	public static final String[] excludedRequestHeaders = new String[] {
		"connection",
		"host"
	};

	public static final String[] excludedResponeHeaders = new String[] {
		"connection"
	};

	public static final String[] excludedJavaGameRequestHeaders = new String[] {
		"connection",
		"host",
		"Accept-Ranges",
		"Content-Range"
	};

	public static final String[] excludedJavaGameResponeHeaders = new String[] {
		"connection",
		"Accept-Ranges",
		"Content-Range",
		"Content-Length"
	};

	
	
	
	@SuppressWarnings("deprecation")
	public Http(final int maxConnections, final int maxConnPerHost) {
		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
		         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
		         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		final PoolingClientConnectionManager connManager = new PoolingClientConnectionManager(schemeRegistry);
		connManager.setMaxTotal(maxConnections);
		connManager.setDefaultMaxPerRoute(maxConnPerHost);
		client = new DefaultHttpClient(connManager, null);
		HttpClientParams.setCookiePolicy(client.getParams(), org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
	}
	
	@SuppressWarnings("deprecation")
	public Http() {
		client = new DefaultHttpClient();
		
		HttpClientParams.setCookiePolicy(client.getParams(), org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 35*60*1000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 35*60*1000);
		
	}
	
	protected com.hungama.net.HttpResponse post(final String URL, final HttpEntity entity, final String[][] headers) 
		throws HttpException{
		HttpPost post = new HttpPost(Encodec.URIEncode(URL));
		post.setHeader(HTTP.USER_AGENT, USER_AGENT);
		if(entity != null) {
			post.setEntity(entity);
		}
		if(headers != null) {
			for(String[] header: headers) {
				post.addHeader(header[0], header[1]);
			}
		}
		try {
			HttpResponse response = client.execute(post);
			StatusLine line = response.getStatusLine();
			switch(line.getStatusCode()) {
			case 301:
			case 302:
			case 307:
				final String redirectURL = response.getFirstHeader(HEADER_LOCATION).getValue();
				if(redirectURL == null || redirectURL.length() == 0) {
					throw new HttpException(line.getStatusCode()+" redirect received without 'Location' header for URL:"+URL);
				}							
				EntityUtils.consume(response.getEntity());							
				post = new HttpPost(Encodec.URIEncode(redirectURL));
				post.setHeader(HTTP.USER_AGENT, USER_AGENT);
				if(entity != null) {
					post.setEntity(entity);
				}
				if(headers != null) {
					for(String[] header: headers) {
						post.addHeader(header[0], header[1]);
					}
				}
				response = client.execute(post);
				line = response.getStatusLine();				
			}
			final com.hungama.net.HttpResponse httpResponse = 
				new com.hungama.net.HttpResponse(line.getStatusCode(), line.getReasonPhrase(), response.getEntity());
			for(Header header : response.getAllHeaders()) {
				httpResponse.setHeader(header.getName(), header.getValue());
			}
			return httpResponse;
		} catch (Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);
		}
	}
	
	protected com.hungama.net.HttpResponse put(final String URL, final HttpEntity entity, final String[][] headers) throws HttpException{
		 HttpPut put = new HttpPut(Encodec.URIEncode(URL));
		put.setHeader(HTTP.USER_AGENT, USER_AGENT);
		if(entity != null) {
			put.setEntity(entity);
		}
		if(headers != null) {
			for(String[] header: headers) {
				put.addHeader(header[0], header[1]);
			}
		}
		try {
			HttpResponse response = client.execute(put);
			StatusLine line = response.getStatusLine();
			switch(line.getStatusCode()) {
			case 301:
			case 302:
			case 307:
				final String redirectURL = response.getFirstHeader(HEADER_LOCATION).getValue(); 
				if(redirectURL == null || redirectURL.length() == 0) {
					throw new HttpException(line.getStatusCode()+" redirect received without 'Location' header for URL:"+URL);
				}							
				EntityUtils.consume(response.getEntity());
				put = new HttpPut(Encodec.URIEncode(redirectURL));
				put.setHeader(HTTP.USER_AGENT, USER_AGENT);
				if(entity != null) {
					put.setEntity(entity);
				}
				if(headers != null) {
					for(String[] header: headers) {
						put.addHeader(header[0], header[1]);
					}
				}
				response = client.execute(put);
				line = response.getStatusLine();				
			}
			final com.hungama.net.HttpResponse httpResponse = 
				new com.hungama.net.HttpResponse(line.getStatusCode(), line.getReasonPhrase(), response.getEntity());
			for(Header header : response.getAllHeaders()) {
				httpResponse.setHeader(header.getName(), header.getValue());
			}
			return httpResponse;
		} catch (Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);
		}
	}
	
	/* protected static String[][] copyRequestHeaders(HttpServletRequest servletRequest) {
	     Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
	    ArrayList<String [] > headers= new ArrayList<String [] >() ;
	     
	    while (enumerationOfHeaderNames.hasMoreElements()) {
	      String headerName = enumerationOfHeaderNames.nextElement();
	      
	    	  if (contains(excludedRequestHeaders, headerName))
	  	        continue;
			     
	     Enumeration<String> eHeaders = servletRequest.getHeaders(headerName);
	    
	      while (eHeaders.hasMoreElements()) {
	    	  String [] header = new String [2];
	        String headerValue = eHeaders.nextElement();

	       // proxyRequest.addHeader(headerName, headerValue);
	        header[0] = headerName;
	       
	         if(headerValue !=null){
	        	
	        	 header[1] = headerValue;
	         	
	         }else{
	        	 header[1] = "";
	         }
	         headers.add(header);
	         
	      }
	    }
	    
	    final String[][] arrayHeaders = new String[headers.size()][];
	    headers.toArray(arrayHeaders);
	    
	    return arrayHeaders;
	  }
	 
	 
	 protected static String[][] copyJavaGameRequestHeaders(HttpServletRequest servletRequest) {
	     Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
	    ArrayList<String [] > headers= new ArrayList<String [] >() ;
	     
	    while (enumerationOfHeaderNames.hasMoreElements()) {
	      String headerName = enumerationOfHeaderNames.nextElement();
	      
	    	  if (contains(excludedJavaGameRequestHeaders, headerName))
	  	        continue;
			     
	     Enumeration<String> eHeaders = servletRequest.getHeaders(headerName);
	    
	      while (eHeaders.hasMoreElements()) {
	    	  String [] header = new String [2];
	        String headerValue = eHeaders.nextElement();

	       // proxyRequest.addHeader(headerName, headerValue);
	        header[0] = headerName;
	       
	         if(headerValue !=null){
	        	
	        	 header[1] = headerValue;
	         	
	         }else{
	        	 header[1] = "";
	         }
	         headers.add(header);
	         
	      }
	    }
	    
	    final String[][] arrayHeaders = new String[headers.size()][];
	    headers.toArray(arrayHeaders);
	    
	    return arrayHeaders;
	  }

	  *//** Copy proxied response headers back to the servlet client. *//*
  protected static void copyResponseHeaders(com.example.Login.net.HttpResponse proxyResponse, HttpServletResponse servletResponse) {
	  Map<String, String[]> headers = proxyResponse.getHeaders();
	  for (Map.Entry<String, String[]> entry : headers.entrySet()) {
		  String [] headerValues =  entry.getValue();
	      for(String headerValue : headerValues ){

	    	  if (contains(excludedResponeHeaders, entry.getKey()))
		  	        continue;
	  	      servletResponse.addHeader(entry.getKey(), headerValue);
	      }
	  }
	    
	  }
  
  protected static void copyJavaGameResponseHeaders(com.example.Login.net.HttpResponse proxyResponse, HttpServletResponse servletResponse) {
	  Map<String, String[]> headers = proxyResponse.getHeaders();
	  for (Map.Entry<String, String[]> entry : headers.entrySet()) {
		  String [] headerValues =  entry.getValue();
	      for(String headerValue : headerValues ){

	    	  if (contains(excludedJavaGameResponeHeaders, entry.getKey()))
		  	        continue;
	  	      servletResponse.addHeader(entry.getKey(), headerValue);
	      }
	  }
	    
	  }

	  *//** Copy response body data (the entity) from the proxy to the servlet client. *//*
  protected  static void copyResponseEntity(com.example.Login.net.HttpResponse proxyResponse, HttpServletResponse servletResponse) throws IOException {

	  try( OutputStream servletOutputStream = servletResponse.getOutputStream();) {
	    	  proxyResponse.writeResponseTo(servletOutputStream);
	      }
	   
	  }

  protected  static void copyJavaGameResponseEntity(com.example.Login.net.HttpResponse proxyResponse, HttpServletResponse servletResponse, String newURL) throws IOException {

	  Properties prop = new Properties();
	  prop.load(proxyResponse.getResponseAsStream());
	  
	  String jarURL = prop.getProperty("MIDlet-Jar-URL");
	  
	 // if (!jarURL.startsWith("http")){
		  
		  newURL = newURL.substring(0, newURL.lastIndexOf("/")+1) + jarURL;
		  
		  URLTokenFactory fact = new URLTokenFactory();
		  
		  newURL =  fact.generateURL(newURL, null, window, salt, System.currentTimeMillis()/1000);
		  
		  prop.setProperty("MIDlet-Jar-URL", "http://akdls3re.hungama.com"+newURL);
	 // }
	  
	  String content = "";
	  
	  for(String key : prop.stringPropertyNames()) {

		  content =  content + key +": " + prop.getProperty(key) + "\n";
		  
		}
	  
	  
	  try( OutputStream servletOutputStream = servletResponse.getOutputStream();
			 ) {
	    	
		  servletOutputStream.write(content.getBytes("UTF-8"));
		  
		  
	      }
	   
	  }
*/
	
	public com.hungama.net.HttpResponse get(final String URL, final String [][]  headers) throws HttpException{
		final HttpGet get = new HttpGet(Encodec.URIEncode(URL));
		get.setHeader(HTTP.USER_AGENT, USER_AGENT);
		if(headers != null) {
			for(String[] header: headers) {
				/*slf4jLog.info(header[0] );
				slf4jLog.info(header[1]);*/
				get.setHeader(header[0], header[1]);
			}
		}
		try {
			final HttpResponse response = client.execute(get);
			final StatusLine line = response.getStatusLine();
			final com.hungama.net.HttpResponse httpResponse = 
				new com.hungama.net.HttpResponse(line.getStatusCode(), line.getReasonPhrase(), response.getEntity());
			for(Header header : response.getAllHeaders()) {
				httpResponse.setHeader(header.getName(), header.getValue());
				//slf4jLog.info("{} : {} ",header.getName(), header.getValue());
			}
			return httpResponse;
		} catch (Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);
		}
	}

	public com.hungama.net.HttpResponse get(final String URL) throws HttpException {
		return get(URL, null);
	}
	
	public com.hungama.net.HttpResponse delete(final String URL, final String[][] headers) 
		throws HttpException{
		HttpDelete delete = new HttpDelete(Encodec.URIEncode(URL));
		delete.setHeader(HTTP.USER_AGENT, USER_AGENT);
		if(headers != null) {
			for(String[] header: headers) {
				delete.addHeader(header[0], header[1]);
			}
		}
		try {
			HttpResponse response = client.execute(delete);
			StatusLine line = response.getStatusLine();
			switch(line.getStatusCode()) {
			case 301:
			case 302:
			case 307:
				final String redirectURL = response.getFirstHeader(HEADER_LOCATION).getValue();
				if(redirectURL == null || redirectURL.length() == 0) {
					throw new HttpException(line.getStatusCode()+" redirect received without 'Location' header for URL:"+URL);
				}							
				EntityUtils.consume(response.getEntity());							
				delete = new HttpDelete(Encodec.URIEncode(redirectURL));
				delete.setHeader(HTTP.USER_AGENT, USER_AGENT);
				if(headers != null) {
					for(String[] header: headers) {
						delete.addHeader(header[0], header[1]);
					}
				}
				response = client.execute(delete);
				line = response.getStatusLine();				
			}
			final com.hungama.net.HttpResponse httpResponse = 
				new com.hungama.net.HttpResponse(line.getStatusCode(), line.getReasonPhrase(), response.getEntity());
			for(Header header : response.getAllHeaders()) {
				httpResponse.setHeader(header.getName(), header.getValue());
			}
			return httpResponse;
		} catch (Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);
		}
	}
	
	public com.hungama.net.HttpResponse delete(final String URL) throws HttpException {
		return delete(URL);
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final String body, final String[][] headers) throws HttpException {
		try {
			return put(URL, new StringEntity(body, "UTF-8"), headers);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final String[][] headers) throws HttpException {
		try {
			return put(URL, (HttpEntity)null, headers);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	

	public com.hungama.net.HttpResponse put(final String URL, final String body, final String contentType) throws HttpException {
		try {
			final StringEntity entity = new StringEntity(body, "UTF-8");
			entity.setContentType(contentType);
			return put(URL, entity, null);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}

	public com.hungama.net.HttpResponse put(final String URL, final InputStream body, final long length, 
			final String[][] headers) throws HttpException {
		try {
			return put(URL, new InputStreamEntity(body, length), headers);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final InputStream body, final long length, 
			final String contentType) throws HttpException {
		try {
			final InputStreamEntity entity = new InputStreamEntity(body, length);
			entity.setContentType(contentType);			
			return put(URL, entity, null);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final InputStream body, final String[][] headers) 
		throws HttpException {
		return put(URL, body, -1, headers);
	}
		
	public com.hungama.net.HttpResponse put(final String URL, final InputStream body, final String contentType) 
		throws HttpException {
		return put(URL, body, -1, contentType);
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final File file, 
			final String[][] headers) throws HttpException {
		try {
			return put(URL, new FileEntity(file, URL), headers);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	public com.hungama.net.HttpResponse put(final String URL, final File file, 
			final String contentType) throws HttpException {
		try {
			return put(URL, new FileEntity(file, ContentType.create(contentType)), null);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	public com.hungama.net.HttpResponse post(final String URL) throws HttpException {
		return post(URL, (HttpEntity)null, (String[][])null);
	}
	
	public com.hungama.net.HttpResponse post(final String URL, final String body) throws HttpException {
		return post(URL, body, (String[][])null);
	}

	public com.hungama.net.HttpResponse get(final String URL, final String body, final String contentType) throws HttpException {
		if(contentType == null) {
			return post(URL, body, (String[][])null);			
		}
		try {
			final StringEntity entity = new StringEntity(body, "UTF-8");
			entity.setContentType(contentType);
			return post(URL, entity, null);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}

	public com.hungama.net.HttpResponse post(final String URL, final String body, final String[][] headers) throws HttpException {
		if(body == null) {
			return post(URL, (HttpEntity)null, headers);				
		}
		try {
			return post(URL, new StringEntity(body, "UTF-8"), headers);
		} catch (HttpException e) {
			throw e;
		} catch(Exception e) {
			throw new HttpException("Exception encountered while calling URL:"+URL, e);			
		}
	}
	
	/*public void proxyRequest(final String proxyRequestUri, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws  URISyntaxException, HttpException, IOException {
		 String method = servletRequest.getMethod();
			try {
				slf4jLog.info("Request URI : {} ",proxyRequestUri);
				slf4jLog.info("Request method : {} ",method);
				
				if(!method.equalsIgnoreCase("GET")){
					throw new HttpException(method+" method not supported by application");
				}

				try(com.example.Login.net.HttpResponse proxyResponse = get(proxyRequestUri, copyRequestHeaders(servletRequest))){
		    	 
				 servletResponse.setStatus(proxyResponse.getStatusCode());
				 
		    	 copyResponseHeaders(proxyResponse, servletResponse);
		    	 
		    	 copyResponseEntity(proxyResponse, servletResponse);
		    	 		    	 
		    	 slf4jLog.info("Response Code  :{}",proxyResponse.getStatusCode());
		     }
		    } catch (HttpException e) {
				throw e;
			} 

		  }
	
	    public void JavaGameRequest(final String proxyRequestUri, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws  URISyntaxException, HttpException, IOException {
		 String method = servletRequest.getMethod();
			try {
				slf4jLog.info("Request URI : {} ",proxyRequestUri);
				slf4jLog.info("Request method : {} ",method);
				
				
				if(!method.equalsIgnoreCase("GET")){
					throw new HttpException(method+" method not supported by application");
				}
				
				String newURI = servletRequest.getRequestURI().replaceAll("/13/431", "/13/429");

				try(com.example.Login.net.HttpResponse proxyResponse = get(proxyRequestUri, copyJavaGameRequestHeaders(servletRequest))){
		    	 
				 servletResponse.setStatus(proxyResponse.getStatusCode());
				 
		    	 copyJavaGameResponseHeaders(proxyResponse, servletResponse);
		    	 
		    	 copyJavaGameResponseEntity(proxyResponse, servletResponse, newURI);
		    	 		    	 
		    	 slf4jLog.info("Response Code  :{}",proxyResponse.getStatusCode());
		     }
		    } catch (HttpException e) {
				throw e;
			} 

		  }
	*/
	public static  boolean contains( final String[] array, final String v ) {
	    for ( final String e : array )
	        if (  v.equalsIgnoreCase(e) )
	            return true;

	    return false;
	}
		 
	
	public void shutdown() {
		client.getConnectionManager().shutdown();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}
}
