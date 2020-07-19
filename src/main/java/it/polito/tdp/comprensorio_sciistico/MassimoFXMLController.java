/**
 * Sample Skeleton for 'CamminoMassimoScene.fxml' Controller Class
 */

package it.polito.tdp.comprensorio_sciistico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.comprensorio_sciistico.model.Impianto;
import it.polito.tdp.comprensorio_sciistico.model.Livello;
import it.polito.tdp.comprensorio_sciistico.model.Model;
import it.polito.tdp.comprensorio_sciistico.model.Tratta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MassimoFXMLController {
	
	private Model model;
	private Stage stage;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtNumeroPartecipanti"
    private TextField txtNumeroPartecipanti; // Value injected by FXMLLoader

    @FXML // fx:id="boxLivello"
    private ComboBox<Livello> boxLivello; // Value injected by FXMLLoader

    @FXML // fx:id="boxLocalitaPartenza"
    private ComboBox<String> boxLocalitaPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="boxImpiantoPartenza"
    private ComboBox<Impianto> boxImpiantoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="boxLoaclitaArrivo"
    private ComboBox<String> boxLocalitaArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="boxImpiantoArrivo"
    private ComboBox<Impianto> boxImpiantoArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="txtOraPartenza"
    private TextField txtOraPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="txtOraArrivo"
    private TextField txtOraArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCamminoMinimo"
    private Button btnCamminoMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="txtErrore"
    private Text txtErrore; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato"
    private TextField txtRisultato; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndietro"
    private Button btnIndietro; // Value injected by FXMLLoader
    
    @FXML
    private TableView<Tratta> tableResult;
    
    @FXML
    private TableColumn<Tratta, String> colonnaTratta;

    @FXML
    private TableColumn<Tratta, String> colonnaNome;

    @FXML
    private TableColumn<Tratta, String> colonnaLocalita;

    @FXML
    private TableColumn<Tratta, String> colonnaTempo;

    
    void doCostruisciGrafo(ActionEvent event) {
    	
    	Livello livello = boxLivello.getValue();
    	if(livello==null) {
    		txtErrore.setText("Selezionare un livello");
    	}else {
    		
    		this.model.creaGrafo(livello);
    		
    		
    	}

    	boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
    	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
    }
    
    @FXML
    void doSceltaPartenza(ActionEvent event) {
    	
    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaPartenza.getValue()));

    }

    @FXML
    void doSceltoArrivo(ActionEvent event) {

    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaArrivo.getValue()));
    }
    
    
    
    @FXML
    void doCalcolaPercorsoMinimo(ActionEvent event) {
    	
    	
    	Integer numeroUtenti = -1;
    	boolean corretto = false;
    	
    	try {
    		
    		numeroUtenti = Integer.parseInt(txtNumeroPartecipanti.getText());
    		corretto = true;
    		
    	}catch(NumberFormatException nfe) {
    		
    		txtErrore.setText("Introdurre un valore numerico intero positivo.");
    	}
    	
    	if(corretto && numeroUtenti > 0) {
	    	String localitaPartenza = boxLocalitaPartenza.getValue();
	    	String localitaArrivo = boxLocalitaArrivo.getValue();
	    	Impianto impiantoPartenza = boxImpiantoPartenza.getValue();
	    	Impianto impiantoArrivo = boxImpiantoArrivo.getValue();
	    	
	    	if(localitaPartenza==null) {
	    		txtErrore.setText("Introdurre una localita di partenza.");
	    	}else if(localitaArrivo==null) {
	    		txtErrore.setText("Introdurre una localita di arrivo.");
	    	}else if(impiantoPartenza==null) {
	    		txtErrore.setText("Introdurre un impianto di partenza.");
	    	}else if(impiantoArrivo==null) {
	    		txtErrore.setText("Introdurre un impianto di arrivo.");
	    	}else {
	    		
	    		//Stampa
	    	
	    		
	    		
	    	}
    	}else {
    		
    		txtErrore.setText("Introdurre un valore positivo.");
    	}

    }

    @FXML
    void getBack(ActionEvent event) {
    	
    	 try {
 	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
 			BorderPane root = (BorderPane)loader.load();
 			
 			FXMLController controller = loader.getController();
 			
 			controller.setStage(this.stage);
 			
 			Scene scene = new Scene(root);
 			
 			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
 			stage.setScene(scene);
 			stage.show();
 			
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 	    

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtNumeroPartecipanti != null : "fx:id=\"txtNumeroPartecipanti\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLivello != null : "fx:id=\"boxLivello\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLocalitaPartenza != null : "fx:id=\"boxLocalitaPartenza\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxImpiantoPartenza != null : "fx:id=\"boxImpiantoPartenza\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLocalitaArrivo != null : "fx:id=\"boxLoaclitaArrivo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxImpiantoArrivo != null : "fx:id=\"boxImpiantoArrivo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtOraPartenza != null : "fx:id=\"txtOraPartenza\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtOraArrivo != null : "fx:id=\"txtOraArrivo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert btnCamminoMinimo != null : "fx:id=\"btnCamminoMinimo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtErrore != null : "fx:id=\"txtErrore\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert btnIndietro != null : "fx:id=\"btnIndietro\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert colonnaTratta != null : "fx:id=\"colonnaTratta\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert colonnaNome != null : "fx:id=\"colonnaNome\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert colonnaLocalita != null : "fx:id=\"colonnaLocalita\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert colonnaTempo != null : "fx:id=\"colonnaTempo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        
    }

	public void setModel(Model model, Stage stage) {
		this.stage= stage;
		this.model = model;
		
    	boxLivello.getItems().add(new Livello("Principiante"));
    	boxLivello.getItems().add(new Livello("Intermedio"));
    	boxLivello.getItems().add(new Livello("Esperto"));
    	
    	boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
    	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
	}
	
}
