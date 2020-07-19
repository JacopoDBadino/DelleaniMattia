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

	private boolean livelloSelezionato = false;
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

    
    /*
    
    @FXML
    private TableColumn<TrattaTabella, String> colonnaTratta;

    @FXML
    private TableColumn<TrattaTabella, String> colonnaNome;

    @FXML
    private TableColumn<TrattaTabella, String> colonnaLocalita;

    @FXML
    private TableColumn<TrattaTabella, String> colonnaTempo;
    */
    
    @FXML
    void doCostruisciGrafo(ActionEvent event) {
    	//this.clearAll();
    	txtRisultato.clear();
    	txtErrore.setText("");
    	boxLocalitaArrivo.getItems().clear();
    	boxLocalitaPartenza.getItems().clear();
    	boxImpiantoArrivo.getItems().clear();
    	boxImpiantoPartenza.getItems().clear();
    	
    	//tableResult.getItems().clear();
    	Livello livello = boxLivello.getValue();
    	if(livello==null) {
    		txtErrore.setText("Selezionare un livello");
    	}else {
    		
    		this.model.creaGrafo(livello);
    		boxLocalitaPartenza.getItems().addAll(this.model.getLocalita());
        	boxLocalitaArrivo.getItems().addAll(this.model.getLocalita());
        	livelloSelezionato = true;
    		
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
    	boxImpiantoPartenza.getItems().clear();
    	boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaPartenza.getValue()));

    }

    @FXML
    void doSceltoArrivo(ActionEvent event) {
    	boxImpiantoArrivo.getItems().clear();
    	boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiPerLocalita(boxLocalitaArrivo.getValue()));

    }

    @FXML
    void doCalcolaPercorsoMinimo(ActionEvent event) {
    	tableResult.getItems().clear();
    	if(livelloSelezionato) {
    	
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
			    		boxImpiantoPartenza.getItems().addAll(this.model.getImpiantiIniziali(localitaPartenza));
			    		boxImpiantoArrivo.getItems().addAll(this.model.getImpiantiIniziali(localitaArrivo));
			    		
			    		impiantoPartenza = boxImpiantoPartenza.getValue();
			    		impiantoArrivo = boxImpiantoArrivo.getValue();
			    		
			    		if(impiantoPartenza==null) {
				    		txtErrore.setText("Introdurre un impianto di partenza.");
				    	}else if(impiantoArrivo==null) {
				    		txtErrore.setText("Introdurre un impianto di arrivo.");
				    	}else {
				    	
				    		txtErrore.setText("");
				    		
				    		//STAMPA RESULT
				    		ObservableList<TrattaTabella> risultato = FXCollections.observableArrayList();
				    		double tempo = 0.0;
				    		List<Tratta> soluzione = this.model.camminiMinimo(impiantoPartenza, impiantoArrivo, numeroUtenti);
				    		System.out.println("SOLUZIONM "+  soluzione.size()+"  "+ soluzione.toString());
				    		if(soluzione.size()!=0) {
					    		for(Tratta t: soluzione ) {
					    			
					    			if(t.getTipo().equals("Pista")) {
					    				Pista p = (Pista)t;
					    				String tratta ="Pista";
					    				tempo+=p.getTempoPercorrenza();
					    				System.out.println(p.getTempoPercorrenza());
					    				risultato.add(new TrattaTabella(tratta,p.getColore(), p.getNome(), p.getLocalita(), convertiTempo(p.getTempoPercorrenza())));
					    			}else {
					    				Impianto i = (Impianto)t;
					    				double secondi = i.getTempoRisalita();
					    				tempo+= secondi;
					    				System.out.println(secondi);
					    				String tratta = "Impianto";
					    				risultato.add(new TrattaTabella(tratta, i.getTipologia(), i.getNome(), i.getLocalita(), convertiTempo(secondi)));
	
					    				
					    			}
					    		}
					    		tableResult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
					    		tableResult.setItems(risultato);
					    		
					    		System.out.println("RIS "+ this.model.getTempoMinimo()+" misurati: " +tempo );
					    		//txtRisultato.setText(convertiTempo(this.model.getTempoMinimo())); NON FUNZIONA CE UNO SCARTO DI SECONDI USANDO DIJK GET WEIGHT
					    		txtRisultato.setText(convertiTempo(this.model.getTempoMinimo()));
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
    			
    			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
        assert boxLivello != null : "fx:id=\"boxLivello\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
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
       
        //assert colonnaTratta != null : "fx:id=\"colonnaTratta\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        //assert colonnaNome != null : "fx:id=\"colonnaNome\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        //assert colonnaLocalita != null : "fx:id=\"colonnaLocalita\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        //assert colonnaTempo != null : "fx:id=\"colonnaTempo\" was not injected: check your FXML file 'CamminoMassimoScene.fxml'.";
        //assert tableResult != null : "fx:id=\"tableResult\" was not injected: check your FXML file 'CamminoMinimoScene.fxml'.";
        
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
    
    
    public void setModel(Model model, Stage stage) {
    	
    	this.stage = stage;
    	this.model= model;
    	
    	boxLivello.getItems().add(new Livello("Principiante"));
    	boxLivello.getItems().add(new Livello("Intermedio"));
    	boxLivello.getItems().add(new Livello("Esperto"));
    	
    }
    
    
 private String convertiTempo(double tempo) {
    	
    	String risultato = "";
    	
    	int ore = (int) Math.floor(tempo/3600);
    	
    	if(ore > 0) {
    		
    		double tempoRestante = (tempo - ore*3600);
    		int minuti = (int)Math.floor(tempoRestante/60);
    		
    		if(minuti > 0) {
    			double secondi = (tempoRestante - (minuti*60));
    			if(secondi >0) {
    				risultato = ore + "h "+minuti+"' "+(int) secondi+"\"";
    			}else {
    				risultato = ore + "h "+minuti+"' ";
    			}
    		}else {
    			int secondi = (int)tempoRestante;
    			
    			risultato = ore+"h " +secondi +"\"";
    			
    		}
    		
    		
    	}else {
    		int minuti = (int) Math.floor(tempo/60);
    		
    		if(minuti >0) {
    			
    			int secondi = (int)(tempo - (minuti*60));
    			if(secondi>0) {
    				risultato = ""+minuti+"' "+secondi+"\"";
    			}else {
    				risultato = ""+minuti+"' ";
    			}
    		}else {
    			int secondi = (int)tempo;
    			
    			risultato =""+secondi +"\"";
    			
    		}
    		
    	}
    	
    	return risultato;
    }
}
 
    


