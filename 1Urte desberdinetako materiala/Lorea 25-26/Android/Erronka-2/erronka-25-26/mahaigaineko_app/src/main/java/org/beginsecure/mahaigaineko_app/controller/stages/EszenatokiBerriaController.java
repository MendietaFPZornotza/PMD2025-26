package org.beginsecure.mahaigaineko_app.controller.stages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.ScenarioClient;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;
import org.beginsecure.mahaigaineko_app.navigation.Navigable;

import java.util.Map;

public class EszenatokiBerriaController implements HasMainController, Navigable {

    @FXML private TextField izenaField;
    @FXML private TextField lekuaField;
    @FXML private TextField aforoaField;
    @FXML private TextField emailaField;
    @FXML private TextField telefonoaField;
    @FXML private Button btnSave;

    private MainLayoutController mainController;

    // Edit moduan gaudenean hemen gordeko dugu ID-a
    private Integer editingStageId = null;

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onNavigate(Map<String, Object> params) {
        // Parametroak badatoz eta stageId badago, edit moduan sartu
        if (params != null && params.containsKey("stageId")) {
            Object v = params.get("stageId");
            Integer id = null;

            if (v instanceof Integer i) id = i;
            else if (v instanceof String s) {
                try { id = Integer.parseInt(s.trim()); } catch (Exception ignore) {}
            }

            if (id != null) {
                loadForEdit(id);
            }
        }
    }

    /**
     * Edit modua aktibatu eta formularioa betetzen du
     */
    private void loadForEdit(int id) {
        this.editingStageId = id;
        btnSave.setText("Eguneratu");

        // Datuak kargatzeko: momentuz GET_SCENARIOS guztiekin bilatzen dugu.
        // (Backend-ean GET_SCENARIO;id badago, hobe hori erabiltzea)
        new Thread(() -> {
            try {
                var list = ScenarioClient.getScenarios();
                ScenarioClient.ScenarioDTO dto = list.stream()
                        .filter(s -> s.id() == id)
                        .findFirst()
                        .orElse(null);

                if (dto == null) {
                    Platform.runLater(() -> showError("Ez da eszenatokirik aurkitu (ID=" + id + ")"));
                    return;
                }

                Platform.runLater(() -> {
                    izenaField.setText(dto.izena());
                    lekuaField.setText(dto.lekua());
                    aforoaField.setText(String.valueOf(dto.aforoa()));
                    emailaField.setText(dto.emaila());
                    telefonoaField.setText(dto.telefonoa());
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showError(e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void saveStage() {
        String izena = safeTrim(izenaField.getText());
        String lekua = safeTrim(lekuaField.getText());
        String aforoRaw = safeTrim(aforoaField.getText());
        String emaila = safeTrim(emailaField.getText());
        String telefonoa = safeTrim(telefonoaField.getText());

        // Balidazioak
        if (izena.isEmpty()) {
            showError("Izena derrigorrezkoa da");
            return;
        }
        if (lekua.isEmpty()) {
            showError("Lekua derrigorrezkoa da");
            return;
        }

        int aforoa;
        try {
            aforoa = Integer.parseInt(aforoRaw);
        } catch (Exception e) {
            showError("Aforoa zenbaki bat izan behar da (adib. 1800)");
            return;
        }

        if (aforoa < 0) {
            showError("Aforoa ezin da negatiboa izan");
            return;
        }

        btnSave.setDisable(true);

        new Thread(() -> {
            try {
                if (editingStageId == null) {
                    // Sortu berria
                    int newId = ScenarioClient.newScenario(izena, lekua, aforoa, emaila, telefonoa);
                    Platform.runLater(() -> {
                        btnSave.setDisable(false);
                        showInfo("Eszenatokia sortuta (ID=" + newId + ")");
                        goBack();
                    });

                } else {
                    // Eguneratu
                    ScenarioClient.updateScenario(editingStageId, izena, lekua, aforoa, emaila, telefonoa);
                    Platform.runLater(() -> {
                        btnSave.setDisable(false);
                        showInfo("Eszenatokia eguneratuta");
                        goBack();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    btnSave.setDisable(false);
                    showError(extractErrorMessage(e.getMessage()));
                });
            }
        }).start();
    }

    @FXML
    private void cancelStage() {
        // Formularioa itxi eta zerrendara bueltatu
        goBack();
    }

    private void goBack() {
        if (mainController != null) {
            mainController.navigate(
                    "/org/beginsecure/mahaigaineko_app/stages/eszenatokiak-view.fxml",
                    (Map<String, Object>) null
            );
        }
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eszenatokia");
        alert.setHeaderText(null);
        alert.setContentText(msg == null || msg.isBlank() ? "Errorea ezezaguna" : msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eszenatokia");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Server-eko ERROR testua garbitu
     */
    private String extractErrorMessage(String raw) {
        if (raw == null) return "Errorea ezezaguna";
        String s = raw.trim();
        if (s.startsWith("ERROR;")) return s.substring(6).trim();
        if (s.startsWith("ERROR:")) return s.substring(6).trim();
        return s.isBlank() ? "Errorea ezezaguna" : s;
    }
}
