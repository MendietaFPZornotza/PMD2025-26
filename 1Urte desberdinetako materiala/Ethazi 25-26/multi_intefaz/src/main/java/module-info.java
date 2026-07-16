module com.mycompany.prototipofroga1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    opens model; 
    opens com.mycompany.prototipofroga1 to javafx.fxml;
    exports com.mycompany.prototipofroga1;
    requires com.google.gson;
  
}
