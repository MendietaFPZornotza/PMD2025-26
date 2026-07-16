package org.beginsecure.mahaigaineko_app.controller.reports;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.beginsecure.mahaigaineko_app.infrastructure.ftp.FTPService;

import java.io.File;
import java.io.FileInputStream;

public class TxostenakController {

    private final FTPService ftp = new FTPService();

    public void uploadPDF(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Aukeratu PDF-a");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        // 🔒 Validar extensión
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            showError("Fitxategia EZ da PDF bat.");
            return;
        }

        // 🔒 Validar cabecera PDF real
        if (!esPdfReal(file)) {
            showError("Fitxategia ez da baliozko PDF bat.");
            return;
        }

        // Tamaño máximo 5 MB
        if (file.length() > 5L * 1024 * 1024) {
            showError("PDF onartutako baino handiagoa da (5 MB).");
            return;
        }

        new Thread(() -> {
            try {
                String remoteName = file.getName();
                String resultado = ftp.uploadPdf(file, remoteName);

                Platform.runLater(() -> {
                    if (resultado != null && resultado.startsWith("PDF ondo igota")) {
                        showInfo(resultado);
                    } else {
                        showError(resultado == null ? "Errorea ezezaguna" : resultado);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> showError("Errorea FTPS: " + e.getMessage()));
            }
        }).start();
    }

    private boolean esPdfReal(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            int n = fis.read(header);
            if (n < 4) return false;
            return new String(header).equals("%PDF");
        } catch (Exception e) {
            return false;
        }
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setTitle("FTPS");
        a.setHeaderText("Ondo");
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setTitle("FTPS");
        a.setHeaderText("Errorea");
        a.showAndWait();
    }
}
