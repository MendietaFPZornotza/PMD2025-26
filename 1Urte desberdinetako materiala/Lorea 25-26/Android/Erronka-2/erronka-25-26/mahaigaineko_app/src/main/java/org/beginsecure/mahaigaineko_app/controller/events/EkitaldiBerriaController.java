package org.beginsecure.mahaigaineko_app.controller.events;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.ftp.FTPService;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.EventClient;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.ScenarioClient;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;
import org.beginsecure.mahaigaineko_app.navigation.Navigable;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.IntStream;

public class EkitaldiBerriaController implements HasMainController, Navigable {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> typeField;
    @FXML private TextArea synopsisArea;
    @FXML private ComboBox<String> eszenatokiCombo;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private ComboBox<Integer> startHourCombo, startMinuteCombo;
    @FXML private ComboBox<Integer> endHourCombo, endMinuteCombo;
    @FXML private Button btnSave;
    @FXML private TextField genreField;
    @FXML private TextField priceArea;

    // ✅ Imagen
    @FXML private Button btnChooseImage;
    @FXML private Label imageStatusLabel;

    private Map<Integer, String> eszenatokiMap;
    private Integer editingEventId;
    private MainLayoutController mainController;

    // ✅ Imagen: nueva seleccionada + path actual (en modo edición)
    private File selectedImage;
    private String currentImagePath = "";

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onNavigate(Map<String, Object> params) {
        if (params != null && params.containsKey("eventId")) {
            loadForEdit((int) params.get("eventId"));
        } else {
            // modo crear
            editingEventId = null;
            currentImagePath = "";
            selectedImage = null;
            if (btnSave != null) btnSave.setText("Guardar");
            if (imageStatusLabel != null) imageStatusLabel.setText("(ez dago irudirik)");
        }
    }

    @FXML
    public void initialize() throws Exception {
        initCombos();
        loadEszenatokiak();

        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());

        if (imageStatusLabel != null) {
            imageStatusLabel.setText("(ez dago irudirik)");
        }
    }

    /**
     * ✅ Aukeratu irudia (png/jpg/jpeg/webp)
     */
    @FXML
    private void chooseImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Aukeratu irudia");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File f = fc.showOpenDialog(btnSave.getScene().getWindow());
        if (f == null) return;

        selectedImage = f;
        if (imageStatusLabel != null) {
            imageStatusLabel.setText("Aukeratuta: " + f.getName());
        }
    }

    /**
     * Ordu/minutu comboak betetzen ditu (minutuak 5eko saltoan).
     */
    private void initCombos() {
        startHourCombo.setItems(FXCollections.observableArrayList(
                IntStream.range(0, 24).boxed().toList()
        ));
        startMinuteCombo.setItems(FXCollections.observableArrayList(
                IntStream.range(0, 60).filter(i -> i % 5 == 0).boxed().toList()
        ));
        endHourCombo.setItems(startHourCombo.getItems());
        endMinuteCombo.setItems(startMinuteCombo.getItems());
    }

    @FXML
    private void saveEvent() {
        try {
            if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
                showError("Izenburua derrigorrezkoa da");
                return;
            }

            if (typeField.getValue() == null) {
                showError("Mota aukeratu behar da");
                return;
            }

            if (eszenatokiCombo.getValue() == null) {
                showError("Eszenatokia aukeratu behar da");
                return;
            }

            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                showError("Data biak zehaztu behar dira");
                return;
            }

            if (startHourCombo.getValue() == null || startMinuteCombo.getValue() == null ||
                    endHourCombo.getValue() == null || endMinuteCombo.getValue() == null) {
                showError("Orduak zehaztu behar dira");
                return;
            }

            LocalDateTime start = LocalDateTime.of(
                    startDatePicker.getValue(),
                    LocalTime.of(startHourCombo.getValue(), startMinuteCombo.getValue())
            );
            LocalDateTime end = LocalDateTime.of(
                    endDatePicker.getValue(),
                    LocalTime.of(endHourCombo.getValue(), endMinuteCombo.getValue())
            );

            if (!end.isAfter(start)) {
                showError("Amaiera data hasiera data baino geroago izan behar da");
                return;
            }

            int eszenatokiId = eszenatokiMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(eszenatokiCombo.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow(() -> {
                        showError("Eszenatokia ez da aurkitu");
                        return new RuntimeException("Eszenatokia ez da aurkitu");
                    });

            // ✅ 1) Resolver imagePath a enviar
            // - En editar: por defecto mantenemos la actual
            // - Si el usuario eligió nueva: subimos y usamos ese nuevo path
            String imagePathToSend = (currentImagePath == null) ? "" : currentImagePath;

            // Deshabilitar botón para evitar doble click durante FTPS
            btnSave.setDisable(true);

            if (selectedImage != null) {
                FTPService ftp = new FTPService();
                imagePathToSend = ftp.uploadEventImage(selectedImage); // devuelve remotePath completo
                if (imageStatusLabel != null) {
                    imageStatusLabel.setText("Igo da: " + imagePathToSend);
                }
            }

            if (imagePathToSend == null) imagePathToSend = "";

            // ✅ 2) Enviar NEW / UPDATE con argazkia al final
            String response = (editingEventId == null)
                    ? EventClient.sendEvent(
                    titleField.getText(), typeField.getValue(), genreField.getText(),
                    start.toString(), end.toString(),
                    synopsisArea.getText(), eszenatokiId, priceArea.getText(),
                    imagePathToSend
            )
                    : EventClient.updateEvent(
                    editingEventId, titleField.getText(), typeField.getValue(), genreField.getText(),
                    start.toString(), end.toString(),
                    synopsisArea.getText(), eszenatokiId, priceArea.getText(),
                    imagePathToSend
            );

            if (response != null && response.startsWith("OK")) {
                mainController.navigate(
                        "/org/beginsecure/mahaigaineko_app/event/ekitaldiak-view.fxml",
                        (java.util.Map<String, Object>) null
                );
            } else if (response != null && response.startsWith("ERROR;")) {
                String errorMessage = extractErrorMessage(response);
                showError(errorMessage);
            } else {
                showError("Errorea ezezaguna: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Errorea: " + e.getMessage());
        } finally {
            if (btnSave != null) btnSave.setDisable(false);
        }
    }

    @FXML
    private void cancelEvent() {
        if (mainController != null) {
            mainController.navigate(
                    "/org/beginsecure/mahaigaineko_app/event/ekitaldiak-view.fxml",
                    (java.util.Map<String, Object>) null
            );
        }
    }

    /**
     * Edit modua aktibatu eta ekitaldiaren datuak kargatzen saiatzen da.
     */
    private void loadForEdit(int id) {
        this.editingEventId = id;
        btnSave.setText("Eguneratu");

        // reset imagen selección
        selectedImage = null;
        currentImagePath = "";

        try {
            String response = EventClient.getEventById(id);
            String[] f = parseGetEventFields(response);

            if (f == null) {
                System.out.println("Ez da aurkitu ekitaldia (ID=" + id + ").");
                return;
            }

            // f[0]=id, f[1]=title, f[2]=type, f[3]=genre, f[4]=start, f[5]=end,
            // f[6]=synopsis, f[7]=eszenatokiId, f[8]=price, (opz) f[9]=argazkia
            titleField.setText(unescapeSafe(getAt(f, 1)));
            typeField.setValue(unescapeSafe(getAt(f, 2)));
            genreField.setText(unescapeSafe(getAt(f, 3)));
            fillDateTimeCombos(getAt(f, 4), true);
            fillDateTimeCombos(getAt(f, 5), false);
            synopsisArea.setText(unescapeSafe(getAt(f, 6)));

            String eszIdRaw = getAt(f, 7);
            if (eszIdRaw != null && !eszIdRaw.isBlank()) {
                int eszId = Integer.parseInt(eszIdRaw.trim());
                String name = eszenatokiMap.get(eszId);
                if (name != null) {
                    eszenatokiCombo.setValue(name);
                }
            }
            priceArea.setText(unescapeSafe(getAt(f, 8)));

            // ✅ Imagen actual si el servidor la envía en GET_EVENT
            if (f.length > 9) {
                String img = getAt(f, 9);
                currentImagePath = (img == null) ? "" : unescapeSafe(img);
            } else {
                currentImagePath = "";
            }

            if (imageStatusLabel != null) {
                if (currentImagePath == null || currentImagePath.isBlank()) {
                    imageStatusLabel.setText("(ez dago irudirik)");
                } else {
                    imageStatusLabel.setText("Irudia: " + currentImagePath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GET_EVENT erantzunetik fields array-a ateratzen du.
     * Onartzen du minimo 9 fields (zaharra) eta 10 fields (berria argazkiarekin).
     */
    private String[] parseGetEventFields(String response) {
        if (response == null || !response.startsWith("OK|")) return null;

        String payload = response.substring(3);
        String[] f = payload.split("\\|", -1);

        if (f.length < 9) return null;
        return f;
    }

    /**
     * Data/ordua string batetik DatePicker eta Combo-ak betetzen ditu.
     */
    private void fillDateTimeCombos(String raw, boolean isStart) {
        if (raw == null || raw.isBlank()) return;

        String v = unescapeSafe(raw).trim();

        String[] parts = v.contains("T") ? v.split("T") : v.split(" ");
        if (parts.length == 0) return;

        try {
            LocalDate date = LocalDate.parse(parts[0]);
            String timeStr = (parts.length > 1) ? parts[1] : "00:00";
            String[] hm = timeStr.split(":");

            int h = Integer.parseInt(hm[0]);
            int m = Integer.parseInt(hm[1]);

            if (isStart) {
                startDatePicker.setValue(date);
                startHourCombo.setValue(h);
                startMinuteCombo.setValue(roundToNearest5(m));
            } else {
                endDatePicker.setValue(date);
                endHourCombo.setValue(h);
                endMinuteCombo.setValue(roundToNearest5(m));
            }
        } catch (Exception ignore) {
        }
    }

    private int roundToNearest5(int m) {
        int r = (int) (Math.round(m / 5.0) * 5);
        if (r == 60) r = 55;
        return r;
    }

    private String getAt(String[] arr, int idx) {
        return (arr != null && idx >= 0 && idx < arr.length) ? arr[idx] : null;
    }

    private String unescapeSafe(String s) {
        if (s == null) return null;
        return s
                .replace("\\n", "\n")
                .replace("\\;", ";")
                .replace("\\\\", "\\");
    }

    private void loadEszenatokiak() throws Exception {
        eszenatokiMap = ScenarioClient.getScenariosMap();
        eszenatokiCombo.setItems(FXCollections.observableArrayList(eszenatokiMap.values()));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ekitaldi berria Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String extractErrorMessage(String serverResponse) {
        if (serverResponse == null || serverResponse.trim().isEmpty()) {
            return "Errorea ezezaguna";
        }

        String response = serverResponse.trim();

        if (response.startsWith("ERROR;")) {
            String errorPart = response.substring(6);
            return errorPart.trim();
        }
        return response;
    }
}
