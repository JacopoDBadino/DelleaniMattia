/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.comprensorio_sciistico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.comprensorio_sciistico.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FXMLController {
	
	private Stage stage;
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCamminoMassimo"
    private Button btnCamminoMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCamminoMinimo"
    private Button btnCamminoMinimo; // Value injected by FXMLLoader

    @FXML
    void doCamminoMassimo(ActionEvent event) {
    	
    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CamminoMassimoScene.fxml"));
			BorderPane root = (BorderPane)loader.load();
			//stage = new Stage();
			MassimoFXMLController controller = loader.<MassimoFXMLController>getController();
			
			Model model = new Model();
			controller.setModel(model,this.stage);
			
			Scene scene = new Scene(root);
			stage.setTitle("CalcolaPercorsi");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			
			} catch (IOException e) {
				e.printStackTrace();
			}

    }

    @FXML
    void doCamminoMinimo(ActionEvent event) {
    	

    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CamminoMinimoScene.fxml"));
			BorderPane root = (BorderPane)loader.load();
			
			MinimoFXMLController controller = loader.<MinimoFXMLController>getController();
			
			Model model = new Model();
			controller.setModel(model,this.stage);
			
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
        assert btnCamminoMassimo != null : "fx:id=\"btnCamminoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCamminoMinimo != null : "fx:id=\"btnCamminoMinimo\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setStage(Stage stage) {

		this.stage= stage;
		
	}
    
   
}


