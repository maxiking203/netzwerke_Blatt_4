package application;

import java.net.URL;
import java.util.ResourceBundle;

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
	
	//Eingabe und Button Paula Pünktlich
	@FXML
	TextField pPStreet;
	@FXML
    TextField pPPLZ; 
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
	TextField lLPLZ;
	@FXML
	TextField lLTime;
	@FXML
	ChoiceBox<String> lLKindOf;
	@FXML
	Button lLDone;
    
	//Eingabe und Button Ich
	@FXML
    TextField iPLZ; 
	@FXML
	TextField iStreet; 
	@FXML
	TextField iTime;
	@FXML
	ChoiceBox<String> iKindOf; 
	@FXML
	Button iDone;
	@Override
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set choice box data for Paula Pünktlich
		pPKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		pPKindOf.setTooltip(new Tooltip("Verkehrsmittel"));
		pPKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Lothar Late
		lLKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		lLKindOf.setTooltip(new Tooltip("Verkehrsmittel"));
		lLKindOf.getSelectionModel().selectFirst();
		
		// set choice box data for Myself
		iKindOf.setItems(FXCollections.observableArrayList("Auto", "zu Fuß", "Rad", "ÖPNV"));
		iKindOf.setTooltip(new Tooltip("Verkehrsmittel"));
		iKindOf.getSelectionModel().selectFirst();
		
	}
	
	public void pPEnd() {
		
	}
	
	public void lLEnd() {
		
	}
	public void iEnd() {
		
	}
	

}
