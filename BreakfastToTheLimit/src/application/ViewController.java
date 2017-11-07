package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ViewController implements Initializable {
	
	private static final String HOME = "Lothstr+34+80335";
	private static final String TEST = "Lindacherstr+14+81249";
	private final String USER_AGENT = "Mozilla/5.0";
	
	//Eingabe und Button Paula Pünktlich
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
	Button insert;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set choice box data for Paula Pünktlich
		pPKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		pPKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Lothar Late
		lLKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		lLKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Myself
		iKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		iKindOf.getSelectionModel().selectFirst();;
		
	}
	
	//Paul Pünktlich left for work
	public void pPEnd() {
		
	}
	
	//Lothar Late left for work
	public void lLEnd() {
		
	}
	
	//I left for work
	public void iEnd() {
		
	}
	
	//Insert finished, start request with input
	public void toSend() {
//		String pAdr = pPStreet.getText() + " " + pPPlace.getText();
//		String pTime = pPTime.getText();
//		String pKind = pPKindOf.getSelectionModel().getSelectedItem();
//		
//		String lAdr = lLStreet.getText() + " " + lLPlace.getText();
//		String lTime = lLTime.getText();
//		String lKind = lLKindOf.getSelectionModel().getSelectedItem();
//		
//		String iAdr  = iStreet.getText() + ", " + iPlace.getText();
//		String iKind = iKindOf.getSelectionModel().getSelectedItem();
//		String strITime  = iTime.getText();
//		
//		doRequest(pAdr, pTime, pKind);
//		doRequest(lAdr, lTime, lKind);
		doRequest();
	}
	
	private void doRequest() {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + HOME + "&destinations=" + TEST + "&key=AIzaSyD5FrhCIemscBuwJYQwoO6wLRuHceirDaY";
			try {
				HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
				con.setRequestMethod("GET");
				//add request header
				//con.setRequestProperty("User-Agent", USER_AGENT);

				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				con.disconnect();

				//print result
				System.out.println(response.toString());
				
			} catch (IOException e) {

				e.printStackTrace();
			}


	}
	

}
