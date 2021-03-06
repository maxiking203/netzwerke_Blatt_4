//Maximilian Sachmann ganze Klasse

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
	
	private boolean running = false;
	
	private long pTime = Long.MAX_VALUE;
	private long lTime = Long.MAX_VALUE;
	private long iTimeS = Long.MAX_VALUE;
	
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
			lightController  = new HueConnection(true);
		}
		catch(IOException e) {
			lightError();
		}
		catch(NullPointerException n) {
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
		catch(NullPointerException n) {
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
		catch(NullPointerException n) {
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
		catch(NullPointerException n) {
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
		if(running) {
			try {
			lightController.checkAllLightColor(times);
			}
			catch(IOException e) {
				lightError();
			}
			catch(NullPointerException n) {
				lightError();
			}
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
		running = false;
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

			
			
			String pAdr = pPStreet.getText().replace(" ", "") + "+" + pPNr.getText() + "+" + pPPlace.getText();
			String tmpTime[] = pPTime.getText().split(":");
			//in Seconds
			try {
			pTime = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			}
			catch (NumberFormatException n) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText("Insert Time");
				alert.setContentText("Please insert the time in the pettern hh:mm!");
				alert.showAndWait();
				return;
			}
			String pMode = getMode(pPKindOf.getSelectionModel().getSelectedItem());
			System.out.println(pMode);
			
			//Einagbe der 2 anderen Adressen zum testen auskommentiert
			String lAdr = lLStreet.getText().replace(" ", "") + "+" + lLNr.getText() + "+" + lLPlace.getText();
			tmpTime = lLTime.getText().split(":");
			try {
			lTime = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			}
			catch (NumberFormatException n) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText("Insert Time");
				alert.setContentText("Please insert the time in the pettern hh:mm!");
				alert.showAndWait();
				return;
			}
			String lMode = getMode(lLKindOf.getSelectionModel().getSelectedItem());
			
			String iAdr  = iStreet.getText().replace(" ", "") + "+" + iNr.getText() + "+" + iPlace.getText();
			tmpTime = iTime.getText().split(":");
			try {
			iTimeS = (Integer.parseInt(tmpTime[0]) * 60 + Integer.parseInt(tmpTime[1])) * 60;
			}
			catch (NumberFormatException n) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText("Insert Time");
				alert.setContentText("Please insert the time in the pettern hh:mm!");
				alert.showAndWait();
				return;
			}
			String iMode = getMode(iKindOf.getSelectionModel().getSelectedItem());
			insert.setDisable(true);

			new Thread (new Runnable() {
				@Override
				public synchronized void run() {
					long pDuration = Long.MAX_VALUE;
					long lDuration = Long.MAX_VALUE;
					long iDuration = Long.MAX_VALUE;
					//
					while(!pLeft || !lLeft || !iLeft) {
						if(!pLeft) {
							try {
							pDuration = doRequest(pAdr, pMode);
							}
							catch (IOException | org.json.simple.parser.ParseException e) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											internalFailure();
					                 }
					             });
								resetAll();
								return;
							}
							catch(NullPointerException n) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											badRequest();
					                 }
					             });
								resetAll();
								return;
							}
							//how much time till, have to leave in Seconds
							 times[0] = getTimeToGo(pTime, pDuration);
							 //
							pPInfo.setText("Abfahrt: " + getLeaveTime(pTime, pDuration) + "\n" + "Dauer(min): " + (pDuration / 60 + 1));
						}
						
						//Einagbe der 2 anderen Adressen zum testen auskommentiert
						if(!lLeft) {
							try {
							lDuration = doRequest(lAdr, lMode);
							}
							catch (IOException | org.json.simple.parser.ParseException e) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											internalFailure();
					                 }
					             });
								resetAll();
								return;
							}
							catch(NullPointerException n) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											badRequest();
					                 }
					             });
								resetAll();
								return;
							}
							//how much time till, have to leave in Seconds
							 times[1] = getTimeToGo(lTime, lDuration);
							 //
							lLInfo.setText("Abfahrt: " + getLeaveTime(lTime, lDuration) + "\n" + "Dauer(min): " + (lDuration / 60 + 1));
						}
						if(!iLeft) {
							try {
							iDuration = doRequest(iAdr, iMode);
							}
							catch (IOException | org.json.simple.parser.ParseException e) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											internalFailure();
					                 }
					             });
								resetAll();
								return;
							}
							catch(NullPointerException n) {
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
											badRequest();
					                 }
					             });
								resetAll();
								return;
							}
							//how much time till, have to leave in Seconds
							 times[2] = getTimeToGo(iTimeS, iDuration);
							 //
							iInfo.setText("Abfahrt: " + getLeaveTime(iTimeS, iDuration) + "\n" + "Dauer(min): " + (iDuration / 60 + 1));
						}
	
						running = true;
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
							catch(NullPointerException n) {
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
	
	private synchronized long doRequest(String adr, String mode) throws IOException, org.json.simple.parser.ParseException, NullPointerException {
		adr.replace("�", "ss");
		adr.replace("�", "ae");
		adr.replace("�", "oe");
		adr.replace("�", "ue");
		adr.replace("�", "Ae");
		adr.replace("�", "Oe");
		adr.replace("�", "Ue");
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + HOME + "&destinations=" + adr + "&mode=" + mode + "&departure_time=now&traffic_mode=best_guess&key=AIzaSyD5FrhCIemscBuwJYQwoO6wLRuHceirDaY";
		System.out.println(url);
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
		alert.setContentText("No Conncetion to philips Hue possible! Please establish connection and restart program.");
		alert.showAndWait();
	}
	
	private void internalFailure() {
		Alert alertF = new Alert(AlertType.INFORMATION);
		alertF.setTitle("Error");
		alertF.setHeaderText("Internal Failure");
		alertF.setContentText("An internal error has occourred. Please try agian.");
		alertF.showAndWait();
		resetAll();
	}
	
	private void badRequest() {
		Alert badRequest = new Alert(AlertType.INFORMATION);
		badRequest.setTitle("Warning");
		badRequest.setHeaderText("Bad Request");
		badRequest.setContentText("With the given Information there was no Request possible. Please Check your inserts!");
		badRequest.showAndWait();
		resetAll();
	}

}
