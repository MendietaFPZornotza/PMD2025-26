package org.beginsecure.mahaigaineko_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MahaigainekoApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MahaigainekoApp.class.getResource("/org/beginsecure/mahaigaineko_app/auth/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750 );
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
