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
	public boolean labor = false;
	public int numberOfLights = 3;
	private int standSat = 254;
	
	public HueConnection(boolean labor) throws IOException {
		this.labor = labor;
		putAllLightsWhite();
		getAllInfo();
	}
	
	public HueConnection() throws IOException {
		putAllLightsWhite();
		getAllInfo();
	}
	
	private JSONObject getAllInfo() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget;
		if (!labor) {
			httpget = new HttpGet("http://" + localIP + "/api/" + localUser + "/lights");
		}
		else {
			httpget = new HttpGet("http://" + lastIP + "/api/" + username + "/lights");
		}
		JSONObject hueJSON = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			try {
				hueJSON = (JSONObject) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
				System.out.println(hueJSON.toString());
			} catch (org.apache.http.ParseException e) {
				System.err.println("Error parsing JSON");
			} catch (ParseException e) {
				System.err.println("Error parsing JSON");
			}
			return hueJSON;
		} catch (IOException e) {
			return hueJSON;
		}
	}
	
	public void checkAllLightColor(long[] seconds) throws IOException {
		int orange = 3500;
		int red = 0;
		int lightNumber = 1;
		String noalert = "none";
		for (long time : seconds) {
			if (time < 0) {
				putAllLightsRedAlert();
				break;
			}
			else {
				if (time <= 120 && time > 60) {
					putLights(lightNumber, orange, noalert, standSat);
				}
				else if (time <= 60 && time > 0) {
					putLights(lightNumber, red, noalert, standSat);
				}
				else {
					putLights(lightNumber, red, noalert, 0);
				}
			}
			lightNumber++;
		}
	}
	
	private void putLights(int id, int color, String alert, int saturation) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut lightput; 
		if (!labor) {
			lightput = new HttpPut("http://" + localIP + "/api/" + localUser + "/lights/" + id + "/state");
		}
		else {
			lightput = new HttpPut("http://" + lastIP + "/api/" + username + "/lights/" + id + "/state");
		}
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
			System.err.println("Error Encoding");
			throw new IOException();
		}
		try {
			HttpResponse response = httpclient.execute(lightput);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("Light values changed");
			}
			else {
				System.err.println("Error setting light value, HTTP Error code:" + response.getStatusLine().getStatusCode());
				throw new IOException();
			}
		} catch (ClientProtocolException e) {
			System.err.println("Error setting light value");
			throw new IOException();
		} catch (IOException e) {
			System.err.println("Error setting light value");
			throw new IOException();
		}
	}
	
	private void putAllLightsColor(int color) throws IOException {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, color, "none", standSat);
		}
	}
	
	private void putAllLightsWhite() throws IOException {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, 0, "none", 0);
		}
	}
	
	private void putAllLightsRedAlert() throws IOException {
		for (int i = numberOfLights; i > 0; i--) {
			putLights(i, 0, "lselect", standSat);
		}
	}
}