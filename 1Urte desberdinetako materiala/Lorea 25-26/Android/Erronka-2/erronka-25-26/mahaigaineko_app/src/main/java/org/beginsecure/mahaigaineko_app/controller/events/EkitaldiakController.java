package org.beginsecure.mahaigaineko_app.controller.events;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.beginsecure.mahaigaineko_app.controller.layout.MainLayoutController;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.EventClient;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.PurchaseClient;
import org.beginsecure.mahaigaineko_app.infrastructure.tcp.ScenarioClient;
import org.beginsecure.mahaigaineko_app.model.dto.EventDTO;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EkitaldiakController implements Initializable, HasMainController {

    @FXML private Button btnAddEvent;
    @FXML private GridPane eventsGrid;
    @FXML private Label placeholderLabel;

    private MainLayoutController mainController;

    @Override
    public void setMainController(MainLayoutController mainController) {
        this.mainController = mainController;
        // Bista kargatzean, ekitaldiak erakutsi.
        loadEvents();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onAddEvent() {
        // Ekitaldi berria sortzeko bistara nabigatu.
        mainController.navigate("/org/beginsecure/mahaigaineko_app/event/ekitaldi-berria.fxml",
                (java.util.Map<String, Object>) null);
    }

    private double parsePrice(String priceRaw) {
        if (priceRaw == null) return 0.0;
        String s = priceRaw.replace("€", "").trim();

        // por si en algún momento viene con coma "12,5€"
        s = s.replace(",", ".");

        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Ekitaldien zerrenda zerbitzaritik ekarri eta grid-ean marrazten du.
     */
    public void loadEvents() {
        eventsGrid.getChildren().clear();

        try {
            String data = EventClient.getEvents();

            if (data == null || data.equals("OK")) {
                placeholderLabel.setVisible(true);
                return;
            }

            // Eszenatokiak 1 aldiz kargatu (aforoa + izena map-ak egiteko)
            List<ScenarioClient.ScenarioDTO> stages = ScenarioClient.getScenarios();

            Map<Integer, Integer> capacityByStage = stages.stream()
                    .collect(Collectors.toMap(
                            ScenarioClient.ScenarioDTO::id,
                            ScenarioClient.ScenarioDTO::aforoa
                    ));

            Map<Integer, String> stageNameById = stages.stream()
                    .collect(Collectors.toMap(
                            ScenarioClient.ScenarioDTO::id,
                            ScenarioClient.ScenarioDTO::izena
                    ));

            String[] events = data.split(";");
            int row = 0, col = 0;

            for (int i = 1; i < events.length; i++) {
                String[] f = events[i].split("\\|", -1);
                EventDTO dto = EventDTO.fromProtocolFields(f);

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/beginsecure/mahaigaineko_app/event/ekitaldi-card.fxml")
                );

                VBox card = loader.load();
                EkitaldiCardController c = loader.getController();

                int capacity = capacityByStage.getOrDefault(dto.getStageId(), 0);
                String stageName = stageNameById.getOrDefault(dto.getStageId(), "—");

                int sold = PurchaseClient.getPurchasedCountByEvent(dto.getId());
                double price = parsePrice(dto.getPrice());
                double income = sold * price;

                c.setEventData(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getStatus(),
                        dto.getDescription(),
                        dto.getDate(),
                        dto.getTime(),
                        stageName,
                        dto.getPrice(),
                        capacity,
                        sold,
                        income,
                        dto.getImagePath()
                );

                c.setMainController(mainController);
                c.setOnDeleted(this::loadEvents);

                eventsGrid.add(card, col, row);
                col = (col + 1) % 2;
                if (col == 0) row++;
            }

            placeholderLabel.setVisible(eventsGrid.getChildren().isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}