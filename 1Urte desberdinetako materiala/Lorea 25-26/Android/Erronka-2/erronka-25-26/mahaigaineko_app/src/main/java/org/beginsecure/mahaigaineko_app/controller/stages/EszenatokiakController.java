package org.beginsecure.mahaigaineko_app.controller.stages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.EventClient;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.ScenarioClient;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class EszenatokiakController implements Initializable, HasMainController {

    @FXML private GridPane gridPane;

    // ✅ Goiko MiniCard-eko label-ak
    @FXML private Label lblTotalStages;
    @FXML private Label lblTotalCapacity;

    private MainLayoutController mainController;

    private boolean viewReady = false;
    private boolean mainReady = false;

    // Header kolore posibleak
    private static final List<String> HEADER_COLOR_CLASSES = List.of(
            "header-blue", "header-purple", "header-green", "header-orange", "header-teal"
    );

    private final Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewReady = true;
        tryLoadStages();
    }

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
        mainReady = true;
        tryLoadStages();
    }

    /**
     * View-a eta MainController prest daudenean bakarrik kargatu
     */
    private void tryLoadStages() {
        if (!viewReady || !mainReady) return;
        Platform.runLater(this::loadStages);
    }

    /**
     * Eszenatoki berria sortzeko bistara joan
     */
    @FXML private void onAddStage() { mainController.navigate( "/org/beginsecure/mahaigaineko_app/stages/eszenatoki-berri.fxml",
            (java.util.Map<String, Object>) null ); }

    /**
     * Eszenatokiak eta ekitaldiak zerbitzaritik kargatu
     */
    public void loadStages() {
        new Thread(() -> {
            try {
                List<ScenarioClient.ScenarioDTO> scenarios = ScenarioClient.getScenarios();

                // Ekitaldi guztiak behin bakarrik eskatu
                String eventsResponse = EventClient.getEvents();
                Map<Integer, List<String>> eventsByStage =
                        parseEventsGroupedByStage(eventsResponse);

                Platform.runLater(() -> {
                    updateHeaderMiniCards(scenarios);
                    renderCards(scenarios, eventsByStage);
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (gridPane != null) gridPane.getChildren().clear();
                    if (lblTotalStages != null) lblTotalStages.setText("—");
                    if (lblTotalCapacity != null) lblTotalCapacity.setText("—");
                });
            }
        }).start();
    }

    /**
     * Goiko MiniCard-ak eguneratu (kopurua eta aforo totala)
     */
    private void updateHeaderMiniCards(List<ScenarioClient.ScenarioDTO> scenarios) {
        if (scenarios == null) scenarios = List.of();

        int totalStages = scenarios.size();
        int totalCapacity = scenarios.stream()
                .mapToInt(ScenarioClient.ScenarioDTO::aforoa)
                .sum();

        lblTotalStages.setText(String.valueOf(totalStages));
        lblTotalCapacity.setText(totalCapacity + " pertsona");
    }

    /**
     * Eszenatoki txartelak marraztu
     */
    private void renderCards(List<ScenarioClient.ScenarioDTO> scenarios,
                             Map<Integer, List<String>> eventsByStage) {

        if (gridPane == null) return;

        gridPane.getChildren().clear();
        int row = 0, col = 0;

        for (int i = 0; i < scenarios.size(); i++) {
            ScenarioClient.ScenarioDTO dto = scenarios.get(i);
            String colorClass = pickHeaderColor(i);

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/org/beginsecure/mahaigaineko_app/stages/eszenatokiak-card.fxml")
                );

                Node card = loader.load();
                EszenatokiCardController c = loader.getController();

                // Eszenatoki honi lotutako ekitaldiak
                List<String> titles = eventsByStage.getOrDefault(dto.id(), List.of());
                String eventsText = formatEventsText(titles);

                c.setStageData(
                        String.valueOf(dto.id()),
                        dto.izena(),
                        dto.lekua(),
                        formatCapacity(dto.aforoa()),
                        eventsText,
                        safeOrDash(dto.telefonoa()),
                        safeOrDash(dto.emaila()),
                        colorClass
                );

                c.setMainController(mainController);
                c.setOnDeleted(this::loadStages);

                gridPane.add(card, col, row);
                col = (col + 1) % 2;
                if (col == 0) row++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ekitaldi zerrenda testu labur bihurtu
     */
    private String formatEventsText(List<String> titles) {
        if (titles == null || titles.isEmpty()) return "—";
        int count = titles.size();
        String preview = titles.stream().limit(3).collect(Collectors.joining(", "));
        return count + " → " + preview + (count > 3 ? "..." : "");
    }

    /**
     * GET_EVENTS_A erantzuna parseatu eta
     * eszenatokiId → ekitaldi-izenak map-a sortu
     */
    private Map<Integer, List<String>> parseEventsGroupedByStage(String response) {
        Map<Integer, List<String>> map = new HashMap<>();
        if (response == null || !response.startsWith("OK")) return map;

        String[] parts = response.split(";", 2);
        if (parts.length < 2) return map;

        for (String item : parts[1].split(";")) {
            String[] f = item.split("\\|", -1);
            if (f.length > 7) {
                Integer stageId = parseIntSafe(f[7], null);
                String title = unescapeSafe(f[1]);
                if (stageId != null && title != null && !title.isBlank()) {
                    map.computeIfAbsent(stageId, k -> new ArrayList<>()).add(title);
                }
            }
        }
        return map;
    }

    private String formatCapacity(int aforoa) {
        return aforoa <= 0 ? "—" : aforoa + " pertsona";
    }

    private String safeOrDash(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    private Integer parseIntSafe(String s, Integer def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private String unescapeSafe(String s) {
        if (s == null) return null;
        return s.replace("\\n", "\n").replace("\\;", ";").replace("\\\\", "\\");
    }

    private String pickHeaderColor(int index) {
        return HEADER_COLOR_CLASSES.get(index % HEADER_COLOR_CLASSES.size());
    }
}
