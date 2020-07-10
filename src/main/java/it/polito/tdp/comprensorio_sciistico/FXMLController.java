package it.polito.tdp.comprensorio_sciistico;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.comprensorio_sciistico.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

	public void setModel(Model model) {
		// TODO Auto-generated method stub
		
	}    
}
