package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ViewController implements Initializable {
	
	private static final String HOME = "Lothstr+34+80335";
	private static final String TEST = "Lindacherstr+14+81249";
	private final String USER_AGENT = "Mozilla/5.0";
	
	//Eingabe und Button Paula P�nktlich
	@FXML
	TextField pPStreet;
	@FXML
	TextField pPNr;
	@FXML
    TextField pPPlace; 
	@FXML
	ChoiceBox<String> pPKindOf; 
	@FXML
	TextField pPTime;
	@FXML
	Button pPDone;
	@FXML
	TextArea pPInfo;
    
	//Eingabe und Button Lothar Late
	@FXML
    TextField lLStreet;
	@FXML
	TextField lLNr;
	@FXML
	TextField lLPlace;
	@FXML
	TextField lLTime;
	@FXML
	ChoiceBox<String> lLKindOf;
	@FXML
	Button lLDone;
	@FXML
	TextArea lLInfo;
    
	//Eingabe und Button Ich
	@FXML
    TextField iPlace; 
	@FXML
	TextField iStreet;
	@FXML
	TextField iNr;
	@FXML
	TextField iTime;
	@FXML
	ChoiceBox<String> iKindOf; 
	@FXML
	Button iDone;
	@FXML
	TextArea iInfo;

	@FXML
	Button reset;
	@FXML
	Button insert;
	
	private boolean pLeft = false;
	private boolean lLeft = false;
	private boolean iLeft = false;
	private long times[] = {Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};
	private HueConnection lightController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set choice box data for Paula P�nktlich
		pPKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fu�", "Rad", "�PNV"));
		pPKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Lothar Late
		lLKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fu�", "Rad", "�PNV"));
		lLKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Myself
		iKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fu�", "Rad", "�PNV"));
		iKindOf.getSelectionModel().selectFirst();;
		
		pPInfo.setEditable(false);
		pPDone.setDisable(true);
		lLInfo.setEditable(false);
		lLDone.setDisable(true);
		iInfo.setEditable(true);
		iDone.setDisable(true);
		
		try {
			lightController  = new HueConnection(false);
		}
		catch(IOException e) {
			lightError();
		}

		
	}
	
	//Paul P�nktlich left for work
	public void pPEnd() {
		pLeft = true;
		pPInfo.setText("Paula already left");
		times[0] = Long.MAX_VALUE;
		try {
		lightController.checkAllLightColor(times);
		}
		catch(IOException e) {
			lightError();
		}
		
	}
	
	//Lothar Late left for work
	public void lLEnd() {
		lLeft = true;
		lLInfo.setText("Lothar already left");
		times[1] = Long.MAX_VALUE;
		try {
		lightController.checkAllLightColor(times);
		}
		catch(IOException e) {
			lightError();
		}
	}
	
	//I left for work
	public void iEnd() {
		iLeft = true;
		iInfo.setText("I already left");
		times[2] = Long.MAX_VALUE;
		try {
		lightController.checkAllLightColor(times);
		}
		catch(IOException e) {
			lightError();
		}
	}
	
	public void resetAll() {
		pLeft = true;
		lLeft = true;
		iLeft = true;
		for(int i = 0; i < times.length; i++) {
			times[i] = Long.MAX_VALUE;
		}
		try {
		lightController.checkAllLightColor(times);
		}
		catch(IOException e) {
			lightError();
		}
		pPStreet.setText("");
		pPNr.setText(""); 
		pPPlace.setText("");
		pPTime.setText("");
		pPStreet.setDisable(false);
		pPNr.setDisable(false); 
		pPPlace.setDisable(false);
		pPTime.setDisable(false);
		pPInfo.setText("");
		insert.setDisable(false);
		
		lLStreet.setText("");
		lLNr.setText(""); 
		lLPlace.setText("");
		lLTime.setText("");
		lLStreet.setDisable(false);
		lLNr.setDisable(false); 
		lLPlace.setDisable(false);
		lLTime.setDisable(false);
		lLInfo.setText("");
		
		iStreet.setText("");
		iNr.setText(""); 
		iPlace.setText("");
		iTime.setText("");
		iStreet.setDisable(false);
		iNr.setDisable(false); 
		iPlace.setDisable(false);
		iTime.setDisable(false);
		iInfo.setText("");
	}
	
	//Insert finished, start request with input
	public void toSend() {
		if(pPStreet.getText().isEmpty() || pPNr.getText().isEmpty() || pPPlace.getText().isEmpty() || pPTime.getText().isEmpty() ||
				lLStreet.getText().isEmpty() || lLNr.getText().isEmpty() || lLPlace.getText().isEmpty() || lLTime.getText().isEmpty() ||
				iStreet.getText().isEmpty() || iNr.getText().isEmpty() || iPlace.getText().isEmpty() || iTime.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Missing Informations");
			alert.setContentText("Your Request cound't start please insert all values!");
			alert.showAndWait();
		}
		else {
			pLeft = false;
			lLeft = false;
			iLeft = false;
			pPDone.setDisable(false);
			lLDone.setDisable(false);
			iDone.setDisable(false);
			insert.setDisable(true);
	
			
			String pAdr = pPStreet.getText().replace(" ", "") + "+" + pPNr.getText() + "+" + pPPlace.getText();
			String tmpTime[] = pPTime.getText().split(":");
			//in Seconds
			long pTime = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			String pMode = getMode(pPKindOf.getSelectionModel().getSelectedItem());
			System.out.println(pMode);
			
			//Einagbe der 2 anderen Adressen zum testen auskommentiert
			String lAdr = lLStreet.getText().replace(" ", "") + "+" + lLNr.getText() + "+" + lLPlace.getText();
			tmpTime = lLTime.getText().split(":");
			long lTime = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			String lMode = getMode(lLKindOf.getSelectionModel().getSelectedItem());
			
			String iAdr  = iStreet.getText().replace(" ", "") + "+" + iNr.getText() + "+" + iPlace.getText();
			tmpTime = iTime.getText().split(":");
			long iTimeS = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			String iMode = getMode(iKindOf.getSelectionModel().getSelectedItem());
			
			new Thread (new Runnable() {
				public synchronized void run() {
					//
					while(!pLeft || !lLeft || !iLeft) {
						if(!pLeft) {
							long pDuration = doRequest(pAdr, pMode);
							//how much time till, have to leave in Seconds
							 times[0] = getTimeToGo(pTime, pDuration);
							 //
							pPInfo.setText("Abfahrt: " + getLeaveTime(pTime, pDuration) + "\n" + "Dauer(min): " + (pDuration / 60 + 1));
						}
						
						//Einagbe der 2 anderen Adressen zum testen auskommentiert
						if(!lLeft) {
							long lDuration = doRequest(lAdr, lMode);
							//how much time till, have to leave in Seconds
							 times[1] = getTimeToGo(lTime, lDuration);
							 //
							lLInfo.setText("Abfahrt: " + getLeaveTime(lTime, lDuration) + "\n" + "Dauer(min): " + (lDuration / 60 + 1));
						}
						if(!iLeft) {
							long iDuration = doRequest(iAdr, iMode);
							//how much time till, have to leave in Seconds
							 times[2] = getTimeToGo(iTimeS, iDuration);
							 //
							iInfo.setText("Abfahrt: " + getLeaveTime(iTimeS, iDuration) + "\n" + "Dauer(min): " + (iDuration / 60 + 1));
						}
	
						
						//
						//
						//Hier k�nntest du dir jetzt mit deiner Methode des long Array times holen
						// index 0 Paula, index 1 Lothar, index 2 Ich
						try {
							lightController.checkAllLightColor(times);
							}
							catch(IOException e) {
								lightError();
							}
						//
						//
						try {
							this.wait(5000);
						} catch (InterruptedException e) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Error");
							alert.setHeaderText("Internal Failure");
							alert.setContentText("An internal error has occourred. Please try agian.");
							alert.showAndWait();
							e.printStackTrace();
							resetAll();
							e.printStackTrace();
						}
					}
					notifyAll();
				}	
			}).start();
		}
	}
	
	private synchronized long getTimeToGo(long time, long duration) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sysTime[] = sdf.format(new Date()).toString().split(":");
		long tmpTime = (Integer.parseInt(sysTime[0]) * 60 + Integer.parseInt(sysTime[1])) * 60 + Integer.parseInt(sysTime[2]);
		time = time - duration - tmpTime;
		System.out.println(time);
		return time;
	}
	
	private synchronized String getLeaveTime(long time, long duration) {
		long tmp = time - duration;
		long hours = (tmp / 60) / 60;
		long min = (tmp - (hours * 60 * 60))/ 60;
		return hours + ":" + min;
	}
	
	private synchronized long doRequest(String adr, String mode) {
		adr.replace("�", "ss");
		adr.replace("�", "ae");
		adr.replace("�", "oe");
		adr.replace("�", "ue");
		adr.replace("�", "Ae");
		adr.replace("�", "Oe");
		adr.replace("�", "Ue");
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + HOME + "&destinations=" + adr + "&mode=" + mode + "&departure_time=now&traffic_mode=best_guess&key=AIzaSyD5FrhCIemscBuwJYQwoO6wLRuHceirDaY";
		System.out.println(url);
			try {
				
				//Verwedung von httpClient
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
				httpclient = null;

				JSONObject jsonObj = (JSONObject) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
				System.out.println(jsonObj);
				JSONArray jsonArray = (JSONArray) new JSONParser().parse((jsonObj.get("rows").toString().toString()));
				jsonObj = (JSONObject) jsonArray.get(0);
				jsonArray = (JSONArray) new JSONParser().parse((jsonObj.get("elements").toString()));
				jsonObj = (JSONObject) jsonArray.get(0);
				if (mode.equals("driving")) {
				jsonObj = (JSONObject) new JSONParser().parse((jsonObj.get("duration_in_traffic").toString()));
				}
				else {
				jsonObj = (JSONObject) new JSONParser().parse((jsonObj.get("duration").toString()));
				}
				return (long) jsonObj.get("value");
				
				
			} catch (IOException e) {
				Alert alertS = new Alert(AlertType.INFORMATION);
				alertS.setTitle("Error");
				alertS.setHeaderText("Internal Failure");
				alertS.setContentText("An internal error has occourred. Please try agian.");
				alertS.showAndWait();
				resetAll();;
				return Long.MAX_VALUE;
			} catch (org.json.simple.parser.ParseException e) {
				Alert alertF = new Alert(AlertType.INFORMATION);
				alertF.setTitle("Error");
				alertF.setHeaderText("Internal Failure");
				alertF.setContentText("An internal error has occourred. Please try agian.");
				alertF.showAndWait();
				resetAll();
				return Long.MAX_VALUE;
			} catch (NullPointerException n) {
//				Alert badRequest = new Alert(AlertType.INFORMATION);
//				badRequest.setTitle("Warning");
//				badRequest.setHeaderText("Bad Request");
//				badRequest.setContentText("With the given Information there was no Request possible. Please Check your inserts!");
//				badRequest.showAndWait();
				System.out.println("NULLPOINTER JSON");
				resetAll();
				return Long.MAX_VALUE;
			}
	}
	
	private String getMode(String kind) {

		if(kind.equals("zu Fu�")) {
			return "walking";
		}
		else if (kind.equals("Rad")) {
			return "bicycling";
		}
		else if (kind.equals("�PNV")) {
			return "transit";
		}
		else {
			return "driving";
		}
		
	}
	
	private void lightError() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ERROR");
		alert.setHeaderText("No Connection");
		alert.setContentText("No Conncetion to philips Hue possible!");
		alert.showAndWait();
	}

}
