package org.beginsecure.mahaigaineko_app.controller.auth;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.LoginClient;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Pane pane;
    @FXML private ImageView imgViewLogo;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Hasierako konfiguraziorik ez oraingoz.
    }

    @FXML
    private void btnLoginAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            // Login eskaera zerbitzarira bidali.
            String response = LoginClient.login(email, password);
            // Erantzuna zatitzen dugu, "LOGIN_OK" bakarrik interesatzen zaigu
            String[] parts = response.split(";");
            if (parts.length > 0 && "LOGIN_OK".equals(parts[0])) {
                // Login ondo: MainLayout kargatu.
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/beginsecure/mahaigaineko_app/layout/main-layout.fxml")
                );

                Parent root = loader.load();

                // Kontrolatzailea lortzen dugu, baina hemen ez dugu ezer gehiago behar.
                MainLayoutController mainController = loader.getController();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                showError("Erabiltzaile edo pasahitza ez dira zuzenak");
            }

        } catch (Exception e) {
            showError("Errorea konexioan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btnExitAction(ActionEvent event) {
        // Aplikazioa itxi.
        Platform.exit();
    }

    /**
     * Errore-mezu estandarra erakusten du.
     */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login errorea");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}