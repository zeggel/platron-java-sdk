package ru.platron.sdk;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class Signature {
	
	private String secretKey;

	public Signature(String secretKey) {
		this.secretKey = secretKey;
	}

	public String md5(String string) {
		MessageDigest messageDigest = null;
	    byte[] digest = new byte[0];
	 
	    try {
	        messageDigest = MessageDigest.getInstance("MD5");
	        messageDigest.reset();
	        messageDigest.update(string.getBytes("UTF-8"));
	        digest = messageDigest.digest();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	 
	    BigInteger bigInt = new BigInteger(1, digest);
	    String md5Hex = bigInt.toString(16);
	 
	    while( md5Hex.length() < 32 ){
	        md5Hex = "0" + md5Hex;
	    }
	 
	    return md5Hex;
	}

	public String prepare(String scriptName, TreeMap<String, String> params) {
		String result = scriptName;
		
		for(String value : params.values()) {
			result += ";" + value;
		}
		
		result += ";" + secretKey;
		return result;
	}
	
	public String make(String scriptName, TreeMap<String, String> params) {
		return md5(prepare(scriptName, params));
	}
}
