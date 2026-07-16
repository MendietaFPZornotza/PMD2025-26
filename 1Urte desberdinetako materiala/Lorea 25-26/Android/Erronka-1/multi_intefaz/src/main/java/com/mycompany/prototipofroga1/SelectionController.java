
package com.mycompany.prototipofroga1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class SelectionController implements Initializable {

    @FXML
    private Button btnKontsultak;
    @FXML
    private Button btnGestioa;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void switchToKontsultak(ActionEvent event) throws IOException {
         App.setRoot("primary");
    }

    @FXML
    private void SwitchToGestioa(ActionEvent event) throws IOException {
         App.setRoot("management");
    }
    
}
