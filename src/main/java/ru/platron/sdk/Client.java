package ru.platron.sdk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.platron.sdk.services.BaseRequest;
import ru.platron.sdk.services.BaseResponse;
import ru.platron.sdk.utils.XmlUtils;

public class Client {
	private String baseUrl = "https://www.platron.ru";
	
	public Object send(BaseRequest request) {
		String url = baseUrl + "/" + request.getScriptName();
		
		String result = null;
		try {
			result = sendXml(url, request.toXml());
		} catch (Exception e) {
			System.out.println("Can't send request");
			e.printStackTrace();
			System.exit(1);
		}
		
		return XmlUtils.fromXml(result, BaseResponse.class, request.getResponseClass());
	}
	
	public String sendXml(String url, String xml) throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		
		String urlParameters = "pg_xml=" + xml;
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
}
