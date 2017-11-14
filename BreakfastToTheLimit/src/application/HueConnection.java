package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HueConnection {
	
	private String lastIP = "10.28.9.123";
	private String username = "2b2d3ff23d63751f10c1d8c0332d50ff";
	private String localIP = "localhost";
	private String localUser = "newdeveloper";
	public boolean local = false;
	
	public JSONObject connect(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget;
		if (url == null) {
			httpget = new HttpGet("http://" + localIP + "/api/" + localUser);
		}
		else {
			httpget = new HttpGet(url);
		}
		JSONObject hueJSON = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			try {
				hueJSON = (JSONObject) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
			} catch (org.apache.http.ParseException e) {

			} catch (ParseException e) {

			}
			httpclient = null;
			getSec(100,0,0);
			return hueJSON;
		} catch (IOException e) {
			httpclient = null;
			return hueJSON;
		}
	}
	
	public void getSec(long p1, long p2, long p3) {
		if (p1 <= 120) {
			putLights(1, 0);
		}
	}
	
	private boolean putLights(int id, int color) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut lightput; 
		lightput = new HttpPut("http://" + localIP + "/api/" + localUser + "/lights/" + id + "/state");
		JSONObject light = new JSONObject();
		light.put("hue", color);
		light.put("on", true);
		try {
			StringEntity message = new StringEntity(light.toString());
			lightput.setEntity(message);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			HttpResponse response = httpclient.execute(lightput);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private boolean putLightsAll() {
		
	}
}