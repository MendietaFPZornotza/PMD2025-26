package org.beginsecure.mahaigaineko_app.controller.events;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.ftp.FTPService;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.EventClient;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Random;

public class EkitaldiCardController {

    // ====== FXML: HEADER ======
    @FXML private StackPane headerPane;

    @FXML private ImageView eventImage; // ✅ tiene que existir en FXML

    @FXML private Label titleLabel;
    @FXML private Label statusLabel;
    @FXML private Label descriptionLabel;

    @FXML private SVGPath btnEdit;
    @FXML private SVGPath btnDelete;

    // ====== FXML: BODY ======
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label venueLabel;
    @FXML private Label priceLabel;

    @FXML private Label ticketsLabel;

    @FXML private Label incomeLabel;
    @FXML private Hyperlink detailsLink;

    private int eventId;
    private Runnable onDeleted;
    private MainLayoutController mainController;

    private static final List<String> HEADER_COLOR_CLASSES = List.of(
            "header-blue",
            "header-purple",
            "header-green",
            "header-orange",
            "header-teal"
    );

    private static final Random RANDOM = new Random();

    /**
     * Txartelaren datuak pantailan jartzen ditu (estatistikekin).
     */
    public void setEventData(int eventId,
                             String title,
                             String status,
                             String description,
                             String date,
                             String time,
                             String venue,
                             String price,
                             int capacity,
                             int sold,
                             double income,
                             String imagePath) {

        this.eventId = eventId;

        titleLabel.setText(safe(title));
        statusLabel.setText(safe(status));
        descriptionLabel.setText(safe(description));

        dateLabel.setText(safe(date));
        timeLabel.setText(safe(time));
        venueLabel.setText(safe(venue));
        priceLabel.setText(formatMoney(parsePriceToDouble(price)));

        // ===== Sarrerak / Aforoa =====
        if (capacity <= 0) {
            ticketsLabel.setText(sold + " / —");
        } else {
            double ratio = Math.min(1.0, sold / (double) capacity);
            int pct = (int) Math.round(ratio * 100);
            ticketsLabel.setText(sold + " / " + capacity + " (" + pct + "%)");
        }

        // ===== Diru-sarrerak =====
        double priceValue = parsePriceToDouble(price);
        double totalIncome = priceValue * sold;
        incomeLabel.setText(formatMoney(totalIncome));

        if (detailsLink != null) {
            detailsLink.setOnAction(e -> System.out.println("Xehetasunak eventId=" + eventId));
        }

        // ✅ imagen por FTPS
        loadImageAsync(imagePath);

        // Header kolorea aleatorioa
        applyRandomHeaderColor();
    }

    /**
     * ✅ Descarga por FTPS en background y pinta en ImageView.
     */
    private void loadImageAsync(String imagePath) {
        if (eventImage == null) return;

        // Limpiar imagen anterior para evitar "flash" si reutilizas cards
        eventImage.setImage(null);

        if (imagePath == null || imagePath.trim().isEmpty() || imagePath.equals("—")) {
            return;
        }

        final String path = imagePath.trim();

        new Thread(() -> {
            try {
                FTPService ftp = new FTPService();
                byte[] data = ftp.downloadBytes(path);

                if (data == null || data.length == 0) return;

                Image img = new Image(new ByteArrayInputStream(data));

                Platform.runLater(() -> {
                    // Si por lo que sea el card se reutilizó para otro evento, evitamos “pisar” imagen
                    if (this.eventId == eventId) {
                        eventImage.setImage(img);
                    } else {
                        eventImage.setImage(img); // fallback (normalmente no pasa)
                    }
                });

            } catch (Exception ignored) {
                Platform.runLater(() -> eventImage.setImage(null));
            }
        }, "ftps-image-" + eventId).start();
    }

    public void setOnDeleted(Runnable onDeleted) {
        this.onDeleted = onDeleted;
    }

    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    private void applyRandomHeaderColor() {
        if (headerPane == null) return;

        headerPane.getStyleClass().removeAll(HEADER_COLOR_CLASSES);
        String color = HEADER_COLOR_CLASSES.get(RANDOM.nextInt(HEADER_COLOR_CLASSES.size()));
        headerPane.getStyleClass().add(color);
    }

    @FXML
    public void onDelete() {
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Ziur zaude ekitaldia ezabatu nahi duzula?",
                ButtonType.OK, ButtonType.CANCEL
        );
        confirm.setTitle("Ekitaldia ezabatu");

        confirm.showAndWait().ifPresent(btn -> {
            if (btn != ButtonType.OK) return;

            new Thread(() -> {
                try {
                    String response = EventClient.deleteEvent(eventId);

                    if (response != null && response.startsWith("OK")) {
                        Platform.runLater(() -> {
                            if (onDeleted != null) onDeleted.run();
                        });
                        return;
                    }

                    String msg = extractErrorMessage(response);

                    Platform.runLater(() -> {
                        if (isEventInUseByTicketsError(msg)) {
                            showError("Ezin da ekitaldia ezabatu, sarrerak erosita daudelako.");
                        } else {
                            showError("Errorea: " + (msg == null || msg.isBlank() ? "Errorea ezezaguna" : msg));
                        }
                    });

                } catch (Exception e) {
                    String msg = (e.getMessage() == null) ? "" : e.getMessage();

                    Platform.runLater(() -> {
                        if (isEventInUseByTicketsError(msg)) {
                            showError("Ezin da ekitaldia ezabatu, sarrerak erosita daudelako.");
                        } else {
                            showError("Errorea zerbitzariarekin: " + (msg.isBlank() ? "konexio-errorea" : msg));
                        }
                    });

                    e.printStackTrace();
                }
            }).start();
        });
    }

    @FXML
    private void onEditEvent() {
        if (mainController != null) {
            mainController.navigate(
                    "/org/beginsecure/mahaigaineko_app/event/ekitaldi-berria.fxml",
                    eventId
            );
        } else {
            showError("Ezin da nabigazioa egin. Berriro saiatu.");
        }
    }

    private String extractErrorMessage(String serverResponse) {
        if (serverResponse == null) return "";
        String s = serverResponse.trim();
        if (s.startsWith("ERROR;")) return s.substring(6).trim();
        if (s.startsWith("ERROR:")) return s.substring(6).trim();
        return s;
    }

    private boolean isEventInUseByTicketsError(String msg) {
        if (msg == null) return false;
        String m = msg.toLowerCase();
        return m.contains("foreign key constraint fails")
                || m.contains("erositako_sarrera")
                || m.contains("ekitaldia_id")
                || m.contains("cannot delete or update a parent row");
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ekitaldi Errorea");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String safe(String s) {
        if (s == null) return "—";
        String t = s.trim();
        return t.isEmpty() ? "—" : t;
    }

    private double parsePriceToDouble(String raw) {
        if (raw == null) return 0.0;

        String s = raw.trim();
        if (s.isEmpty() || s.equals("—")) return 0.0;

        s = s.replace("€", "").trim();
        s = s.replace(",", ".");

        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String formatMoney(double v) {
        return String.format(java.util.Locale.US, "%.2f €", v);
    }
}
