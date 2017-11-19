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
	public int numberOfLights = 3;
	private int standSat = 254;
	
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
			putAllLightsWhite();
			return hueJSON;
		} catch (IOException e) {
			httpclient = null;
			return hueJSON;
		}
	}
	
	//orange = 5000
	//red = 0
	//white =
	public void checkAllLightColor(long[] seconds) {
		int orange = 5000;
		int red = 0;
		int lightNumber = 1;
		String alert = "none";
		for (long time : seconds) {
			if (time < 0) {
				putAllLightsRedAlert();
				break;
			}
			else {
				if (time <= 120 && time >= 60) {
					putLights(lightNumber, orange, alert, standSat);
				}
				else if (time <= 60) {
					putLights(lightNumber, red, alert, standSat);
				}
			}
			lightNumber++;
		}
	}
	
	private boolean putLights(int id, int color, String alert, int saturation) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut lightput; 
		lightput = new HttpPut("http://" + localIP + "/api/" + localUser + "/lights/" + id + "/state");
		JSONObject light = new JSONObject();
		light.put("hue", color);
		light.put("on", true);
		light.put("alert", alert);
		light.put("bri", 254);
		light.put("sat", saturation);
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
	
	private void putAllLightsColor(int color) {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, color, "none", standSat);
		}
	}
	
	private void putAllLightsWhite() {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, 0, "none", 0);
		}
	}
	
	private void putAllLightsRedAlert() {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, 0, "select", standSat);
		}
	}
}