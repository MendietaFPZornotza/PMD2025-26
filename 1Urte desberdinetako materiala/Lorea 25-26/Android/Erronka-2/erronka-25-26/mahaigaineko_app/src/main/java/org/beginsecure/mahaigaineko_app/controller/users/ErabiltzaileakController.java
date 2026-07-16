package org.beginsecure.mahaigaineko_app.controller.users;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.UserClient;
import org.beginsecure.mahaigaineko_app.model.domain.User;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ErabiltzaileakController implements HasMainController {

    @FXML private VBox erabiltzaileakPane;
    @FXML private TextField textFieldBilatuErabiltzailea;
    @FXML private TableView<User> erabiltzaileakTaula;

    // MiniCards (Label-ak)
    @FXML private Label lblTotalUsers;
    @FXML private Label lblActiveUsers;
    @FXML private Label lblAdmins;
    @FXML private Label lblTotalEvents;

    // FXML berriko zutabeak
    @FXML private TableColumn<User, User> colErabiltzailea;
    @FXML private TableColumn<User, String> colRola;
    @FXML private TableColumn<User, LocalDateTime> colAzkenSaioa;
    @FXML private TableColumn<User, Void> colEkintzak;

    private ObservableList<User> erabiltzaileakList;
    private ObservableList<User> filteredList;

    private MainLayoutController mainController;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
        loadErabiltzaileak();
        setupSearchFilter();

        // Hasierako balioak (UI ez apurtzeko)
        if (lblTotalUsers != null) lblTotalUsers.setText("—");
        if (lblActiveUsers != null) lblActiveUsers.setText("—");
        if (lblAdmins != null) lblAdmins.setText("—");
        if (lblTotalEvents != null) lblTotalEvents.setText("—");
    }

    private void setupTableColumns() {

        // ===== ERABILTZAILEA: izena + emaila =====
        colErabiltzailea.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
        colErabiltzailea.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(User u, boolean empty) {
                super.updateItem(u, empty);

                if (empty || u == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label izena = new Label(nullToEmpty(u.getIzena()));
                izena.getStyleClass().add("userName");

                Label emaila = new Label(nullToEmpty(u.getEmaila()));
                emaila.getStyleClass().add("userEmail");

                VBox box = new VBox(2, izena, emaila);
                setText(null);
                setGraphic(box);
            }
        });

        // ===== ROLA: pill =====
        colRola.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(nullToEmpty(data.getValue().getMota()))
        );
        colRola.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String mota, boolean empty) {
                super.updateItem(mota, empty);

                if (empty || mota == null || mota.isBlank() || mota.equals("—")) {
                    setText("—");
                    setGraphic(null);
                    return;
                }

                Label pill = new Label(mota.toLowerCase());
                pill.getStyleClass().add("pill");

                if ("ADMIN".equalsIgnoreCase(mota)) {
                    pill.getStyleClass().add("pill-admin");
                } else {
                    pill.getStyleClass().add("pill-user");
                }

                setText(null);
                setGraphic(pill);
                setAlignment(Pos.CENTER_LEFT);
            }
        });

        // ===== AZKEN SAIOA: icono + fecha =====
        colAzkenSaioa.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getCreatedAt())
        );
        colAzkenSaioa.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dt, boolean empty) {
                super.updateItem(dt, empty);

                if (empty || dt == null) {
                    setText("—");
                    setGraphic(null);
                    return;
                }

                SVGPath clock = new SVGPath();
                clock.setContent("M12 2a10 10 0 1 0 0.001 20.001A10 10 0 0 0 12 2zm1 11h4v-2h-3V6h-2v7z");
                clock.setFill(javafx.scene.paint.Color.web("#94a3b8"));
                clock.setScaleX(0.75);
                clock.setScaleY(0.75);

                Label text = new Label(DATE_FORMATTER.format(dt));
                text.getStyleClass().add("dateText");

                HBox box = new HBox(6, clock, text);
                box.getStyleClass().add("dateBox");

                setText(null);
                setGraphic(box);
                setAlignment(Pos.CENTER_LEFT);
            }
        });
    }

    // ===== EKINTZAK: =====
    private void setupActionColumn() {
        colEkintzak.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditatu = createIconButton("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zm2.92 2.83H5v-.92l9.06-9.06.92.92L5.92 20.08zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z", "#4c51bf");
            private final Button btnEzabatu = createIconButton("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zm2.46-7.12 1.41-1.41L12 12.59l2.12-2.12 1.41 1.41L13.41 14l2.12 2.12-1.41 1.41L12 15.41l-2.12 2.12-1.41-1.41L10.59 14 8.46 11.88zM15.5 4l-1-1h-5l-1 1H5v2h14V4z", "#e84646");
            private final HBox hBox = new HBox(5); // Reducir espacio entre botones

            {
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(btnEditatu, btnEzabatu);

                btnEditatu.setOnAction(event -> {
                    User u = getTableView().getItems().get(getIndex());
                    editatuErabiltzailea(u);
                });

                btnEzabatu.setOnAction(event -> {
                    User u = getTableView().getItems().get(getIndex());
                    ezabatuErabiltzailea(u);
                });
            }

            private Button createIconButton(String svgPath, String color) {
                Button button = new Button();

                javafx.scene.shape.SVGPath svg = new javafx.scene.shape.SVGPath();
                svg.setContent(svgPath);
                svg.setFill(javafx.scene.paint.Color.web(color));
                svg.setStroke(javafx.scene.paint.Color.TRANSPARENT);

                svg.setScaleX(0.8);
                svg.setScaleY(0.8);

                button.setGraphic(svg);
                button.getStyleClass().add("icon-button");

                return button;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void editatuErabiltzailea(User u) {
        if (mainController != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", u.getId());
            mainController.navigate("/org/beginsecure/mahaigaineko_app/user/erabiltzaile-berria.fxml", params);
        } else {
            System.err.println("MainController null da, ezin da nabigatu");
        }
    }

    private void ezabatuErabiltzailea(User u) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmazioa");
        alert.setHeaderText("Erabiltzailea ezabatu");
        alert.setContentText("Ziur zaude " + nullToEmpty(u.getIzena()) + " ezabatu nahi duzula?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        UserClient.deleteUser(u.getId());

                        Platform.runLater(() -> {
                            // Taulatik kendu
                            if (erabiltzaileakList != null) erabiltzaileakList.remove(u);
                            if (filteredList != null) filteredList.remove(u);

                            // ✅ MiniCards eguneratu
                            updateMiniCards();

                            Alert ok = new Alert(Alert.AlertType.INFORMATION);
                            ok.setTitle("Arrakasta");
                            ok.setHeaderText(null);
                            ok.setContentText("Erabiltzailea zuzen ezabatu da");
                            ok.showAndWait();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();

                        String raw = (e.getMessage() == null) ? "" : e.getMessage().trim();
                        String code = raw;

                        // Soportar "ERROR:DELETE_FAIL" y "ERROR;DELETE_FAIL"
                        if (code.startsWith("ERROR:")) code = code.substring(6).trim();
                        if (code.startsWith("ERROR;")) code = code.substring(6).trim();

                        final String msg;
                        if ("DELETE_FAIL".equalsIgnoreCase(code)) {
                            msg = "Ezin da erabiltzailea ezabatu, sarrerak erosita dituelako.";
                        } else {
                            // Fallback neutro (sin tecnicismos)
                            msg = "Ezin izan da erabiltzailea ezabatu. Saiatu berriro.";
                        }

                        Platform.runLater(() -> {
                            Alert err = new Alert(Alert.AlertType.ERROR);
                            err.setTitle("Errorea");
                            err.setHeaderText("Ezin da ezabatu");
                            err.setContentText(msg);
                            err.showAndWait();
                        });
                    }
                }).start();
            }
        });
    }

    private void loadErabiltzaileak() {
        new Thread(() -> {
            try {
                var users = UserClient.getUsers();

                Platform.runLater(() -> {
                    erabiltzaileakList = FXCollections.observableArrayList(users);
                    filteredList = FXCollections.observableArrayList(users);
                    erabiltzaileakTaula.setItems(filteredList);

                    // ✅ MiniCards eguneratu
                    updateMiniCards();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        System.err.println("Errorea erabiltzaileak kargatzerakoan: " + e.getMessage())
                );
            }
        }).start();
    }

    private void setupSearchFilter() {
        textFieldBilatuErabiltzailea.textProperty().addListener((obs, oldValue, newValue) ->
                filterErabiltzaileak(newValue)
        );
    }

    private void filterErabiltzaileak(String searchText) {
        if (erabiltzaileakList == null) return;

        if (searchText == null || searchText.isBlank()) {
            filteredList.setAll(erabiltzaileakList);
            return;
        }

        String lower = searchText.toLowerCase();
        filteredList.setAll(
                erabiltzaileakList.stream()
                        .filter(u ->
                                safe(u.getIzena()).contains(lower) ||
                                        safe(u.getEmaila()).contains(lower) ||
                                        safe(u.getMota()).contains(lower)
                        )
                        .toList()
        );
    }

    @FXML
    private void btnFiltratuErabiltzaileak() {
        System.out.println("Filtratu (oraingoz ez dago implementatuta)");
    }

    public void refresh() {
        loadErabiltzaileak();
    }

    /**
     * ✅ MiniCards kalkulatu eta UI-n jarri.
     * Datu errealak: total + admins.
     * Aktiboak / Ekitaldiak: ez daukagu daturik oraindik -> "—"
     */
    private void updateMiniCards() {
        if (lblTotalUsers == null || lblAdmins == null) return;
        if (erabiltzaileakList == null) return;

        int total = erabiltzaileakList.size();

        int admins = (int) erabiltzaileakList.stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getMota()))
                .count();

        lblTotalUsers.setText(String.valueOf(total));
        lblAdmins.setText(String.valueOf(admins));

        if (lblActiveUsers != null) lblActiveUsers.setText("—");   // no hay campo active
        if (lblTotalEvents != null) lblTotalEvents.setText("—");   // no hay EventClient aquí
    }

    private String safe(String s) {
        return (s == null) ? "" : s.toLowerCase();
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    @FXML
    public void btnAddUser(ActionEvent actionEvent) {
        mainController.navigate("/org/beginsecure/mahaigaineko_app/user/erabiltzaile-berria.fxml",
                (java.util.Map<String, Object>) null);
    }
}
