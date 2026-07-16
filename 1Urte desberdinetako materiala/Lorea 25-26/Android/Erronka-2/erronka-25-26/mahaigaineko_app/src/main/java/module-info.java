module org.beginsecure.mahaigaineko_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.net;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires javafx.graphics;
    requires javafx.base;
    requires kotlin.stdlib;
    requires java.desktop;

    opens org.beginsecure.mahaigaineko_app to javafx.fxml;
    exports org.beginsecure.mahaigaineko_app;
    opens org.beginsecure.mahaigaineko_app.controller.layout to javafx.fxml;
    exports org.beginsecure.mahaigaineko_app.controller.layout;
    opens org.beginsecure.mahaigaineko_app.controller.auth to javafx.fxml;
    exports org.beginsecure.mahaigaineko_app.controller.auth;
    exports org.beginsecure.mahaigaineko_app.controller.events;
    opens org.beginsecure.mahaigaineko_app.controller.events to javafx.fxml;
    opens org.beginsecure.mahaigaineko_app.controller.users to javafx.fxml;
    exports org.beginsecure.mahaigaineko_app.controller.users;
    opens org.beginsecure.mahaigaineko_app.controller.stages to javafx.fxml;
    exports org.beginsecure.mahaigaineko_app.controller.stages;
    exports org.beginsecure.mahaigaineko_app.controller.reports;
    opens org.beginsecure.mahaigaineko_app.controller.reports to javafx.fxml;
}