package org.beginsecure.mahaigaineko_app.controller.layout;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class SidebarController implements Initializable {

    @FXML private AnchorPane sidebar;

    @FXML private Button btnEkitaldiak;
    @FXML private Button btnEszenatokiak;
    @FXML private Button btnErabiltzaileak;
    @FXML private Button btnTxostenak;
    @FXML private Button btnIrten;

    private MainLayoutController mainController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ez dago hasierako logikarik momentuz.
    }

    /**
     * Sidebar-etik nabigatzeko, MainLayoutController erreferentzia gordetzen du.
     */
    public void setMainController(MainLayoutController controller) {
        this.mainController = controller;
    }

    @FXML
    private void onEkitaldiak() {
        // Ekitaldien zerrendara joan.
        mainController.navigate("/org/beginsecure/mahaigaineko_app/event/ekitaldiak-view.fxml", (Map<String, Object>) null);
    }

    @FXML
    private void onEszenatokiak() {
        // Eszenatokien bistara joan.
        mainController.navigate("/org/beginsecure/mahaigaineko_app/stages/eszenatokiak-view.fxml", (Map<String, Object>) null);
    }

    @FXML
    private void onErabiltzaileak() {
        // Erabiltzaileen bistara joan.
        mainController.navigate("/org/beginsecure/mahaigaineko_app/user/erabiltzaileak-view.fxml", (Map<String, Object>) null);
    }

    @FXML
    private void onTxostenak() {
        // Txostenen bistara joan.
        mainController.navigate("/org/beginsecure/mahaigaineko_app/reports/txostenak-view.fxml", (Map<String, Object>) null);
    }

    @FXML
    private void onIrten() {
        // Aplikazioa itxi.
        Platform.exit();
    }
}