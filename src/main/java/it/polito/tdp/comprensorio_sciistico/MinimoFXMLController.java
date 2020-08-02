/**
 * Sample Skeleton for 'CamminoMinimoScene.fxml' Controller Class
 */

package it.polito.tdp.comprensorio_sciistico;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.comprensorio_sciistico.model.Impianto;
import it.polito.tdp.comprensorio_sciistico.model.Livello;
import it.polito.tdp.comprensorio_sciistico.model.Model;
import it.polito.tdp.comprensorio_sciistico.model.Pista;
import it.polito.tdp.comprensorio_sciistico.model.Tratta;
import it.polito.tdp.comprensorio_sciistico.model.TrattaTabella;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MinimoFXMLController {
	
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
    
    @FXML
    private RadioButton btnRadioStazionePartenza;

    @FXML
    private RadioButton btnRadioStazioneArrivo;

    @FXML // fx:id="boxLoaclitaArrivo"
    private ComboBox<String> boxLocalitaArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="boxImpiantoArrivo"
    private ComboBox<Impianto> boxImpiantoArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCamminoMinimo"
    private Button btnCamminoMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="txtErrore"
    private Text txtErrore; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato"
    private TextField txtRisultato; // Value injected by FXMLLoader

    @FXML // fx:id="btnIndietro"
    private Button btnIndietro; // Value injected by FXMLLoader
    @FXML
    private TableView<TrattaTabella> tableResult;
    
    @FXML
    private RadioButton btnRadioPartenza;

    @FXML
    private RadioButton btnRadioArrivo;
    
    
    @FXML
    void doCostruisciGrafo(ActionEvent event) {
    	
    	tableResult.getItems().clear();
    	
    	
    	txtRisultato.clear();
    	txtErrore.setText("");
    	boxLocalitaArrivo.getItems().clear();
    	boxLocalitaPartenza.getItems().clear();
    	boxImpiantoArrivo.getItems().clear();
    	boxImpiantoPartenza.getItems().clear();
    	
    	Livello livello = boxLivello.getValue();
    	if(livello==null) {
    		txtErrore.setText("Selezionare un livello");
    	}else {
    		
    		this.model.creaGrafo(livello);
    		
    		boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
        	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
        	
        	boxLocalitaArrivo.setDisable(false);
        	boxLocalitaPartenza.setDisable(false);
 
    	}

    }
    
    @FXML
    void doCaricaInizialiArrivo(MouseEvent event) {
    	if(btnRadioArrivo.isSelected()){
	    	boxImpiantoArrivo.getItems().clear();
	    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaArrivo.getValue()));
    	}else {
    		boxImpiantoArrivo.getItems().clear();
    		boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaArrivo.getValue()));
    	}
    	
    }

    @FXML
    void doCaricaInizialiPartenza(MouseEvent event) {
    	if(btnRadioPartenza.isSelected()){
	    	boxImpiantoPartenza.getItems().clear();
	    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaPartenza.getValue()));
	    }else {
    		boxImpiantoPartenza.getItems().clear();
    		boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaPartenza.getValue()));
    	}
    	
    }
    
   
    
    @FXML
    void doSceltaPartenza(ActionEvent event) {
    	
    	btnRadioPartenza.setDisable(false);
    	btnRadioStazionePartenza.setDisable(false);
    	boxImpiantoPartenza.setDisable(false);
    	if(btnRadioPartenza.isSelected()){
	    	boxImpiantoPartenza.getItems().clear();
	    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaPartenza.getValue()));
	    }else {
	    	boxImpiantoPartenza.getItems().clear();
	    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaPartenza.getValue()));
	    }
    }

    @FXML
    void doSceltoArrivo(ActionEvent event) {
    	
    	boxImpiantoArrivo.setDisable(false);
    	btnRadioStazioneArrivo.setDisable(false);
    	btnRadioArrivo.setDisable(false);
    	if(btnRadioArrivo.isSelected()){
	    	boxImpiantoArrivo.getItems().clear();
	    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaArrivo.getValue()));
    	}else {
	    	boxImpiantoArrivo.getItems().clear();
	    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaArrivo.getValue()));
    	}
    }

    @FXML
    void doCalcolaPercorsoMinimo(ActionEvent event) {
    	tableResult.getItems().clear();
    	if(boxLivello.getValue()!=null) {
    	
	    	Integer numeroUtenti = -1;
	    	boolean corretto = false;
	    	
	    	try {
	    		numeroUtenti = Integer.parseInt(txtNumeroPartecipanti.getText());
	    		corretto = true;
	    		
	    	}catch(NumberFormatException nfe) {
	    		
	    		txtErrore.setText("Introdurre un valore numerico intero positivo di partecipanti.");
	    	}
	    	
	    	if(corretto) {
	    		if(numeroUtenti > 0) {
	    	
			    	String localitaPartenza = boxLocalitaPartenza.getValue();
			    	String localitaArrivo = boxLocalitaArrivo.getValue();
			    	Impianto impiantoPartenza = null;
			    	Impianto impiantoArrivo = null;
			    	
			    	if(localitaPartenza==null) {
			    		txtErrore.setText("Introdurre una localita di partenza.");
			    	}else if(localitaArrivo==null) {
			    		txtErrore.setText("Introdurre una localita di arrivo.");
			    	}else {
			    		
			    		impiantoPartenza = boxImpiantoPartenza.getValue();
			    		impiantoArrivo = boxImpiantoArrivo.getValue();
			    		
			    		if(impiantoPartenza==null) {
				    		txtErrore.setText("Introdurre un impianto di partenza.");
				    	}else if(impiantoArrivo==null) {
				    		txtErrore.setText("Introdurre un impianto di arrivo.");
				    	}else {
				    	
				    		txtErrore.setText("");
				    		
				    		ObservableList<TrattaTabella> risultato = FXCollections.observableArrayList();
				    		String idStazionePartenza ="";
				    		String idStazioneArrivo = "";
				    		if(btnRadioStazionePartenza.isSelected()) {
				    			idStazionePartenza =impiantoPartenza.getIdValle();
				    		}else {
				    			idStazionePartenza = impiantoPartenza.getIdMonte();
				    		}
				    		
				    		if(btnRadioStazioneArrivo.isSelected()) {
				    			idStazioneArrivo = impiantoArrivo.getIdValle();
				    		}else {
				    			idStazioneArrivo = impiantoArrivo.getIdMonte();
				    		}
				    		List<Tratta> soluzione = this.model.camminoMinimo(idStazionePartenza, idStazioneArrivo, numeroUtenti);
				    		if(soluzione.size()!=0) {
					    		for(Tratta t: soluzione ) {
					    			
					    			if(t.getTipo().equals("Pista")) {
					    				Pista p = (Pista)t;
					    				String tratta ="Pista";
					    				risultato.add(new TrattaTabella(tratta,p.getColore(), p.getNome(), p.getLocalita(), this.model.convertiTempoInStringa(p.getTempoPercorrenza())));
					    			}else {
					    				Impianto i = (Impianto)t;
					    				double secondi = i.getTempoRisalita();
					    				String tratta = "Impianto";
					    				risultato.add(new TrattaTabella(tratta, i.getTipologia(), i.getNome(), i.getLocalita(), this.model.convertiTempoInStringa(secondi)));
	
					    			}
					    		}
					    		tableResult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
					    		tableResult.setItems(risultato);
					    		txtRisultato.setText(this.model.convertiTempoInStringa(this.model.getTempoMinimo()));
				    		}else {
				    			
				    			txtErrore.setText("Per il livello selezionato non è possibile determinare un percorso tra i due impianti");
				    		}
					
				    	}
			    	}
		    	}else {
		    		
		    		txtErrore.setText("Introdurre un valore positivo di partecipanti.");
		    	}
	    	}
	    	
    	
    	}else {
    		
    		txtErrore.setText("E' necessario selezionare un livello per far funzionare l'applicazione");
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
    			
    			stage.setScene(scene);
    			stage.show();
    			
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    	    

    }
    @SuppressWarnings("unchecked")
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtNumeroPartecipanti != null : "fx:id=\"txtNumeroPartecipanti\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert boxLivello != null : "fx:id=\"boxLivello\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLocalitaPartenza != null : "fx:id=\"boxLocalitaPartenza\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert boxImpiantoPartenza != null : "fx:id=\"boxImpiantoPartenza\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert boxLocalitaArrivo != null : "fx:id=\"boxLoaclitaArrivo\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert boxImpiantoArrivo != null : "fx:id=\"boxImpiantoArrivo\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnCamminoMinimo != null : "fx:id=\"btnCamminoMinimo\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert txtErrore != null : "fx:id=\"txtErrore\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnIndietro != null : "fx:id=\"btnIndietro\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnRadioPartenza != null : "fx:id=\"btnRadioPartenza\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnRadioArrivo != null : "fx:id=\"btnRadioArrivo\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnRadioStazionePartenza != null : "fx:id=\"btnRadioStazionePartenza\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        assert btnRadioStazioneArrivo != null : "fx:id=\"btnRadioStazioneArrivo\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
       
       
        
        TableColumn<TrattaTabella, String> colonnaTratta = new TableColumn<>("Tratta");
        colonnaTratta.setCellValueFactory(new PropertyValueFactory<TrattaTabella, String>("tipo"));
        TableColumn<TrattaTabella, String> colonnaNome = new TableColumn<>("Nome");
        colonnaNome.setCellValueFactory(new PropertyValueFactory<TrattaTabella, String>("nome"));
        TableColumn<TrattaTabella, String> colonnaLocalita = new TableColumn<>("Località");
        colonnaLocalita.setCellValueFactory(new PropertyValueFactory<TrattaTabella, String>("localita"));
        TableColumn<TrattaTabella, String> colonnaTempo = new TableColumn<>("Tempo");
        colonnaTempo.setCellValueFactory(new PropertyValueFactory<TrattaTabella, String>("tempo"));
        TableColumn<TrattaTabella, String> colonnaDescrizione = new TableColumn<>("Descrizione");
        colonnaDescrizione.setCellValueFactory(new PropertyValueFactory<TrattaTabella, String>("descrizione"));
        
        tableResult.getColumns().addAll(colonnaTratta, colonnaDescrizione, colonnaNome, colonnaLocalita, colonnaTempo);
        tableResult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
    }
    
    
    private void setAll(boolean valore) {
    	
    	btnRadioArrivo.setDisable(valore);
    	btnRadioPartenza.setDisable(valore);
    	
    	boxImpiantoArrivo.setDisable(valore);
    	boxImpiantoPartenza.setDisable(valore);
    	
    	boxLocalitaArrivo.setDisable(valore);
    	boxLocalitaPartenza.setDisable(valore);
    	
    	btnRadioStazioneArrivo.setDisable(valore);
    	btnRadioStazionePartenza.setDisable(valore);
    }
    
    public void setModel(Model model, Stage stage) {
    	
    	this.stage = stage;
    	this.model= model;
    	
    	boxLivello.getItems().add(new Livello("Principiante"));
    	boxLivello.getItems().add(new Livello("Intermedio"));
    	boxLivello.getItems().add(new Livello("Esperto"));
    	
    	setAll(true);
    	
    }
    

}
 
    


