package it.polito.tdp.comprensorio_sciistico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MassimoFXMLController {
	
	private Stage stage;
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtNumeroPartecipanti;

    @FXML
    private ComboBox<Livello> boxLivello;

    @FXML
    private ComboBox<String> boxLocalitaPartenza;

    @FXML
    private ComboBox<Impianto> boxImpiantoPartenza;

    @FXML
    private ComboBox<String> boxLocalitaArrivo;

    @FXML
    private ComboBox<Impianto> boxImpiantoArrivo;

    @FXML
    private ComboBox<Integer> boxOre;

    @FXML
    private ComboBox<Integer> boxMinuti;

    @FXML
    private Button btnCamminoMinimo;

    @FXML
    private Text txtErrore;

    @FXML
    private TableView<TrattaTabella> tableResult;

    @FXML
    private TextField txtRisultato;

    @FXML
    private Button btnIndietro;

    @FXML
    void doCalcolaPercorsoMassimo(ActionEvent event) {
    	txtRisultato.clear();
    	tableResult.getItems().clear();
    	txtErrore.setText("");
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
			    		//boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(localitaPartenza));
			    		//boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(localitaArrivo));
			    		
			    		impiantoPartenza = boxImpiantoPartenza.getValue();
			    		impiantoArrivo = boxImpiantoArrivo.getValue();
			    		
			    		if(impiantoPartenza==null) {
				    		txtErrore.setText("Introdurre un impianto di partenza.");
				    	}else if(impiantoArrivo==null) {
				    		txtErrore.setText("Introdurre un impianto di arrivo.");
				    	}else {
				    		Integer tempoMassimoAttivita = 0;
				    		if(boxMinuti.getValue()==null && boxOre.getValue()==null) {
				    			txtErrore.setText("Selezionare minuti e/o ore di attività");
				    			return;
				    		}else if(boxMinuti.getValue()==null && boxOre.getValue()!=null){
				    			tempoMassimoAttivita = boxOre.getValue()*3600;
				    		}else if(boxMinuti.getValue()!=null && boxOre.getValue()==null) {
				    			tempoMassimoAttivita = boxMinuti.getValue()*60;
				    		}else {
				    			tempoMassimoAttivita = boxMinuti.getValue()*60 + boxOre.getValue()*3600;
				    		}
				    		
				    		if(tempoMassimoAttivita<= 8*3600 && tempoMassimoAttivita > 0.0) {
					    		txtErrore.setText("");
					    		
					    		//STAMPA RESULT
					    		ObservableList<TrattaTabella> risultato = FXCollections.observableArrayList();
					    		
					    		List<Tratta> soluzione = this.model.trovaMassimoPercorso(boxLivello.getValue(), numeroUtenti, tempoMassimoAttivita, impiantoPartenza, impiantoArrivo);
					    		
					    		if(soluzione!=null) {
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
						    		txtRisultato.setText("" + this.model.convertiTempoInStringa(this.model.getTempoBest()));
					    		}else {
					    			txtErrore.setText("Per il livello selezionato e la durata selezionata non è possibile determinare un percorso tra i due impianti");

					    		}
				    		}else {
				    			txtErrore.setText("La durata dell'attività deve essere maggiore di 0 minuti e non puo' superare le 8 ore");
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
    		
    		
    		boxLocalitaArrivo.setDisable(false);
        	boxLocalitaPartenza.setDisable(false);
        	
    		boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
        	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
    	}
        	
        
        	
        	
    		
        	
    }

    @FXML
    void doSceltaPartenza(ActionEvent event) {
    	
    	boxImpiantoPartenza.getItems().clear();
    	boxImpiantoPartenza.setDisable(false);
    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaPartenza.getValue()));
    	
    	
    	boxOre.setDisable(false);
    	boxMinuti.setDisable(false);


    }

    @FXML
    void doSceltoArrivo(ActionEvent event) {

    	
    	boxImpiantoArrivo.setDisable(false);
    	boxImpiantoArrivo.getItems().clear();
    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(boxLocalitaArrivo.getValue()));
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
	@FXML
    void initialize() {
        assert txtNumeroPartecipanti != null : "fx:id=\"txtNumeroPartecipanti\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLivello != null : "fx:id=\"boxLivello\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLocalitaPartenza != null : "fx:id=\"boxLocalitaPartenza\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxImpiantoPartenza != null : "fx:id=\"boxImpiantoPartenza\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxLocalitaArrivo != null : "fx:id=\"boxLocalitaArrivo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxImpiantoArrivo != null : "fx:id=\"boxImpiantoArrivo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxOre != null : "fx:id=\"boxOre\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert boxMinuti != null : "fx:id=\"boxMinuti\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert btnCamminoMinimo != null : "fx:id=\"btnCamminoMinimo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtErrore != null : "fx:id=\"txtErrore\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert tableResult != null : "fx:id=\"tableResult\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        assert btnIndietro != null : "fx:id=\"btnIndietro\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";

        
        
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
    	
    	boxImpiantoArrivo.setDisable(valore);
    	boxImpiantoPartenza.setDisable(valore);
    	
    	boxLocalitaArrivo.setDisable(valore);
    	boxLocalitaPartenza.setDisable(valore);
    	
    	boxOre.setDisable(valore);
    	boxMinuti.setDisable(valore);
    	
    }
	public void setModel(Model model, Stage stage) {
		this.stage= stage;
		this.model = model;
		
    	boxLivello.getItems().add(new Livello("Principiante"));
    	boxLivello.getItems().add(new Livello("Intermedio"));
    	boxLivello.getItems().add(new Livello("Esperto"));
    	
    	boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
    	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
    	List<Integer> ore = new ArrayList<>();
    	List<Integer> minuti = new ArrayList<>();
    	
    	for(int i =0; i<=8; i ++) {
    		
    		ore.add(i);
    	}
    	
    	for(int i =0; i<=59; i++) {
    		
    		minuti.add(i);
    	
    	}
    	
    	boxOre.getItems().addAll(ore);
    	boxMinuti.getItems().addAll(minuti);
    	
    	setAll(true);
    	
	}
	
}

