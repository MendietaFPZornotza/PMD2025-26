package org.beginsecure.mahaigaineko_app.controller.layout;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.beginsecure.mahaigaineko_app.navigation.HasMainController;
import org.beginsecure.mahaigaineko_app.navigation.Navigable;

import java.util.Map;

public class MainLayoutController {

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    private SidebarController sidebarIncludeController;

    @FXML
    private void initialize() {
        // Sidebar-ari "mainController" ematen diogu, bertatik nabigatu ahal izateko.
        if (sidebarIncludeController != null) {
            sidebarIncludeController.setMainController(this);
        }

        // Hasierako bista kargatu.
        navigate("/org/beginsecure/mahaigaineko_app/event/ekitaldiak-view.fxml", (Map<String, Object>) null);
    }

    /**
     * Bista baten nabigazioa egiten du eta, behar izanez gero, parametroak pasatzen dizkio kontrolatzaileari.
     */
    public void navigate(String fxmlPath, Map<String, Object> params) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            System.out.println("Is HasMainController? " + (loader.getController() instanceof HasMainController));
            Object controller = loader.getController();

            // Controller-ak MainLayout behar badu, injektatu.
            if (controller instanceof HasMainController c) {
                c.setMainController(this);
            }

            // Controller-a "Navigable" bada, parametroak pasatu.
            if (controller instanceof Navigable nav) {
                nav.onNavigate(params);
            }

            // Bista mainContentPane barruan txertatu eta ankerrak ondo finkatu.
            mainContentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            System.out.println("Is HasMainController? " + (loader.getController() instanceof HasMainController));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Overload erabilgarria: eventId bakarrik pasatuta, Map formatuan bidaltzen du.
     * (EkitaldiCardController-etik datorren deia ez apurtzeko.)
     */
    public void navigate(String fxmlPath, Integer eventId) {
        Map<String, Object> params = (eventId == null) ? null : Map.of("eventId", eventId);
        navigate(fxmlPath, params);
    }
}