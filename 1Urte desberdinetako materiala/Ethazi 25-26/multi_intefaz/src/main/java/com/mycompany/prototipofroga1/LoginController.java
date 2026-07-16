
package com.mycompany.prototipofroga1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController implements Initializable {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Button primaryButton;
    @FXML
    private Label txtMsgError;
    private Stage stage;
    private String nickAccepted = "Ane";
    private String passAccepted ="Ane123";
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //oculta mensaje de error
        txtMsgError.setVisible(false);
        txtUser.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
             if (newValue){
                txtMsgError.setVisible(false);
             }
        });
        txtPass.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
             if (newValue){
                txtMsgError.setVisible(false);
             }
        });
        
        
        
    }    

    @FXML
    private void switchToSelection(ActionEvent event) throws IOException {
          if((nickAccepted .equals(txtUser.getText())) &&(passAccepted.equals(txtPass.getText()))){
                  App.setRoot("selection");
             }else{
                 txtMsgError.setVisible(true);
             }
        
    }
    
}
