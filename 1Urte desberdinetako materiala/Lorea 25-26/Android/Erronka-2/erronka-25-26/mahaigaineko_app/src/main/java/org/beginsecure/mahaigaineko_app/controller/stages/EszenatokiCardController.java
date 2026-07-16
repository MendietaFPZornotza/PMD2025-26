package org.beginsecure.mahaigaineko_app.controller.stages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.ScenarioClient;

import java.util.List;

public class EszenatokiCardController {

    @FXML private StackPane headerPane;

    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label addressLabel;
    @FXML private Label capacityLabel;
    @FXML private Label eventsLabel;
    @FXML private Label phoneLabel;
    @FXML private Hyperlink emailLabel;

    private MainLayoutController mainController;
    private Runnable onDeleted;

    private String eszenatokiId;

    private static final List<String> HEADER_COLOR_CLASSES = List.of(
            "header-blue", "header-purple", "header-green", "header-orange", "header-teal"
    );

    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    public void setOnDeleted(Runnable onDeleted) {
        this.onDeleted = onDeleted;
    }

    public void setStageData(
            String id, String name,
            String address, String capacity, String events,
            String phone, String email,
            String headerColorClass
    ) {
        this.eszenatokiId = id;

        nameLabel.setText(safeOrDash(name));

        // Estos 2 estaban en FXML pero no los estabas llenando:
        typeLabel.setText("Eszenatokia");

        addressLabel.setText(safeOrDash(address));
        capacityLabel.setText(safeOrDash(capacity));
        eventsLabel.setText(safeOrDash(events));
        phoneLabel.setText(safeOrDash(phone));
        emailLabel.setText(safeOrDash(email));

        applyHeaderColor(headerColorClass);

        emailLabel.setOnAction(e -> System.out.println("Email: " + email));
    }

    private void applyHeaderColor(String cls) {
        headerPane.getStyleClass().removeAll(HEADER_COLOR_CLASSES);
        headerPane.getStyleClass().add((cls == null || cls.isBlank()) ? "header-blue" : cls);
    }

    @FXML
    private void onDelete() {
        // Confirmación (igual estilo que users)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmazioa");
        alert.setHeaderText("Eszenatokia ezabatu");
        alert.setContentText("Ziur zaude eszenatoki hau ezabatu nahi duzula?");

        alert.showAndWait().ifPresent(response -> {
            if (response != ButtonType.OK) return;

            new Thread(() -> {
                try {
                    int id = Integer.parseInt(eszenatokiId);
                    ScenarioClient.deleteScenario(id);

                    Platform.runLater(() -> {
                        if (onDeleted != null) onDeleted.run();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = (e.getMessage() == null) ? "" : e.getMessage();

                    Platform.runLater(() -> {
                        Alert err = new Alert(Alert.AlertType.ERROR);
                        err.setTitle("Errorea");
                        err.setHeaderText("Ezin izan da ezabatu");

                        // Detectar “está en uso” de forma robusta
                        if (isScenarioInUseError(msg)) {
                            err.setContentText("Ezin da eszenatokia ezabatu, ekitaldi batean erabiltzen ari delako.");
                        } else {
                            err.setContentText(msg.isBlank() ? "Errorea ezezaguna" : msg);
                        }

                        err.showAndWait();
                    });
                }
            }).start();
        });
    }

    private boolean isScenarioInUseError(String msg) {
        if (msg == null) return false;
        String m = msg.toLowerCase();

        return m.contains("foreign key")
                || m.contains("constraint")
                || m.contains("referenc")
                || m.contains("in use")
                || m.contains("used")
                || m.contains("cannot delete")
                || m.contains("ekitaldi")
                || m.contains("event");
    }

    @FXML
    private void onEdit() {
        // Editatzeko bistara joan, stageId parametroarekin
        if (mainController == null) {
            System.out.println("MainController null: ezin da nabigatu.");
            return;
        }

        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("stageId", Integer.parseInt(eszenatokiId)); // hobe int bezala

        mainController.navigate(
                "/org/beginsecure/mahaigaineko_app/stages/eszenatoki-berri.fxml",
                params
        );
    }

    private String safeOrDash(String s) {
        if (s == null) return "—";
        String t = s.trim();
        return t.isEmpty() ? "—" : t;
    }
}