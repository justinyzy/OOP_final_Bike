module org.example.ubkie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires com.google.gson;
    requires javafx.web;
    requires java.desktop;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens org.example.ubkie to javafx.fxml;
    exports org.example.ubkie;

    exports org.example.ubkie.GetAndSet;
    opens org.example.ubkie.GetAndSet to com.fasterxml.jackson.databind;

    exports org.example.ubkie.Controller;
    opens org.example.ubkie.Controller to javafx.fxml;

    exports org.example.ubkie.Controller.User;
    opens org.example.ubkie.Controller.User to javafx.fxml;

    exports org.example.ubkie.Controller.Staff;
    opens org.example.ubkie.Controller.Staff to javafx.fxml;
}
