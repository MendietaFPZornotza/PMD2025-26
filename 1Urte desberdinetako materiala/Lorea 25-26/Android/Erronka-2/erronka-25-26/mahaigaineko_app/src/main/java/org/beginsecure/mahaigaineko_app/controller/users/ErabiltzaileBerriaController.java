package org.beginsecure.mahaigaineko_app.controller.users;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.UserClient;
import org.beginsecure.mahaigaineko_app.model.domain.User;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;
import org.beginsecure.mahaigaineko_app.navigation.Navigable;

import java.util.Map;
import java.util.List;

public class ErabiltzaileBerriaController implements HasMainController, Navigable {

    @FXML private TextField izenaField;
    @FXML private TextField emailaField;
    @FXML private PasswordField pasahitzaField;
    @FXML private ComboBox<String> motaCombo;
    @FXML private Button btnSave;

    private MainLayoutController mainController;
    private Integer editingUserId = null;

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onNavigate(Map<String, Object> params) {
        // Parametroak badatoz, edit moduan sartzen gara.
        if (params != null && params.containsKey("userId")) {
            loadForEdit((int) params.get("userId"));
        }
    }

    @FXML
    public void initialize() {
        // Valor por defecto opcional
        if (motaCombo != null && motaCombo.getValue() == null && !motaCombo.getItems().isEmpty()) {
            motaCombo.setValue(motaCombo.getItems().get(0));
        }
    }

    @FXML
    private void saveUser() {
        String izena = safeTrim(izenaField.getText());
        String emaila = safeTrim(emailaField.getText());
        String pass = safeTrim(pasahitzaField.getText());
        String mota = motaCombo.getValue();

        // Validaciones
        if (izena.isEmpty()) {
            showError("Izena derrigorrezkoa da");
            return;
        }
        if (emaila.isEmpty()) {
            showError("Emaila derrigorrezkoa da");
            return;
        }
        if (!looksLikeEmail(emaila)) {
            showError("Email formatu okerra da");
            return;
        }
        if (mota == null || mota.isBlank()) {
            showError("Mota aukeratu behar da");
            return;
        }

        btnSave.setDisable(true);

        new Thread(() -> {
            try {
                String response;

                if (editingUserId != null) {
                    // Modo edición: UPDATE_USER
                    response = callUpdateUser(editingUserId, izena, emaila, pass, mota);
                } else {
                    // Modo creación: NEW_USER
                    response = callNewUser(izena, emaila, pass, mota);
                }

                Platform.runLater(() -> {
                    btnSave.setDisable(false);

                    if (response.startsWith("OK")) {
                        String successMessage = editingUserId != null
                                ? "Erabiltzailea zuzen eguneratu da"
                                : "Erabiltzailea zuzen sortu da";

                        showInfo(successMessage);
                        goBackToUsers();
                    } else if (response.startsWith("ERROR;")) {
                        // Zerbitzariko errorea erakutsi
                        String errorMessage = extractErrorMessage(response);
                        showError(errorMessage);
                    } else {
                        String errorMessage = editingUserId != null
                                ? "Ezin izan da erabiltzailea eguneratu"
                                : "Ezin izan da erabiltzailea sortu";

                        showError(errorMessage);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnSave.setDisable(false);
                    showError(extractErrorMessage(e.getMessage()));
                });
            }
        }).start();
    }

    private String callNewUser(String izena, String emaila, String pass, String mota) throws Exception {
        boolean ok = UserClient.newUser(izena, emaila, pass, mota);
        return ok ? "OK" : "ERROR;Ezin izan da sortu";
    }

    private String callUpdateUser(Integer id, String izena, String emaila, String pass, String mota) throws Exception {
        boolean ok = UserClient.updateUser(id, izena, emaila, pass, mota);
        return ok ? "OK" : "ERROR;Ezin izan da eguneratu";
    }

    /**
     * Edit modua aktibatu eta erabiltzailearen datuak kargatzen saiatzen da.
     */
    private void loadForEdit(int id) {
        this.editingUserId = id;
        btnSave.setText("Eguneratu");

        new Thread(() -> {
            try {
                // Obtener todos los usuarios y filtrar el que necesitamos
                List<User> users = UserClient.getUsers();
                User targetUser = users.stream()
                        .filter(u -> u.getId() == id)
                        .findFirst()
                        .orElse(null);

                Platform.runLater(() -> {
                    if (targetUser != null) {
                        loadUserData(targetUser);
                    } else {
                        showError("Erabiltzailea ez da aurkitu (ID=" + id + ")");
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Errorea erabiltzailea kargatzerakoan: " + extractErrorMessage(e.getMessage()));
                });
            }
        }).start();
    }

    /**
     * Carga datos de un usuario en el formulario
     */
    private void loadUserData(User user) {
        if (user == null) return;

        izenaField.setText(safeTrim(user.getIzena()));
        emailaField.setText(safeTrim(user.getEmaila()));

        // En modo edición, el campo de contraseña puede estar vacío
        // (el usuario puede cambiarla o dejarla igual)
        pasahitzaField.setText("");
        pasahitzaField.setPromptText("Utzi hutsik ez aldatzeko");

        // Seleccionar el tipo correcto en el ComboBox
        if (user.getMota() != null) {
            motaCombo.setValue(user.getMota());
        }
    }

    @FXML
    private void cancelUser() {
        goBackToUsers();
    }

    private void goBackToUsers() {
        if (mainController != null) {
            mainController.navigate(
                    "/org/beginsecure/mahaigaineko_app/user/erabiltzaileak-view.fxml",
                    (java.util.Map<String, Object>) null
            );
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(editingUserId != null ? "Erabiltzailea editatu" : "Erabiltzaile berria");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(editingUserId != null ? "Erabiltzailea editatu" : "Erabiltzaile berria");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean looksLikeEmail(String email) {
        // Simple y suficiente para UI
        return email.contains("@") && email.contains(".") && !email.contains(" ");
    }

    private String extractErrorMessage(String raw) {
        if (raw == null) return "Errorea ezezaguna";
        String s = raw.trim();

        // Tu server manda "ERROR;mensaje"
        if (s.startsWith("ERROR;")) {
            return s.substring(6).trim();
        }
        if (s.startsWith("ERROR:")) {
            return s.substring(6).trim();
        }

        return s.isEmpty() ? "Errorea ezezaguna" : s;
    }
}