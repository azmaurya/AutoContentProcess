package com.hungama.net;


import java.io.UnsupportedEncodingException;

public class Encodec {
	private static final String ESCAPE_CHARACTERS = "\"<>[\\]^`{|} ";
	private static final String hexChar = "0123456789ABCDEF";

	public static String URIEncodeNonASCII(final String URL) {
		if(URL == null) return null;
		final byte[] bytes;
		try {
			bytes = URL.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
		final byte[] retVal = new byte[bytes.length*3];
		int i = 0, c = 0;
		for(byte b: bytes) {
			c = (b & 0xFF);
            if((c >= '0' && c <= '9') || 
            	(c >= 'A' && c <= 'Z') || 
            	(c >= 'a' && c <= 'z')) {
            	retVal[i++] = (byte)c;
                continue;
            }
            retVal[i++] = '%';
            retVal[i++] = (byte)hexChar.charAt(c/16);
            retVal[i++] = (byte)hexChar.charAt(c%16);            
		}
		try {
			return new String(retVal, 0, i, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static String URIEncode(final String URL) {
		if(URL == null) return null;
		final byte[] bytes;
		try {
			bytes = URL.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
		final byte[] retVal = new byte[bytes.length*3];
		int i = 0, c = 0;
		for(byte b: bytes) {
			c = (b & 0xFF);
            if(c >= '!' && c <= '~' && 
                    (ESCAPE_CHARACTERS.indexOf(c) < 0)) {
            	retVal[i++] = (byte)c;
                continue;
            }
            retVal[i++] = '%';
            retVal[i++] = (byte)hexChar.charAt(c/16);
            retVal[i++] = (byte)hexChar.charAt(c%16);            
		}
		try {
			return new String(retVal, 0, i, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public static String URIDecode(final String URL) {
		if(URL == null) return null;
		final byte[] bytes;
		try {
			bytes = URL.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
		final byte[] retVal = new byte[bytes.length];
		int pos = 0, c = 0;
		for(int i = 0; i < bytes.length; i++) {
			c = (bytes[i] & 0xFF);
            if(c == '%') {
            	retVal[pos++] = (byte)((16*(hexChar.indexOf(bytes[i+1]))) + hexChar.indexOf(bytes[i+2]));
            	i+=2;
            } else {
            	retVal[pos++] = bytes[i];
            }
		}
		try {
			return new String(retVal, 0, pos, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
}
