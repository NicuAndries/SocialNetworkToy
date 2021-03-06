module com.example.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.example.socialnetwork to javafx.fxml;
    opens com.example.socialnetwork.controller to javafx.fxml;
    opens com.example.socialnetwork.domain to javafx.fxml;
    opens com.example.socialnetwork.dto to javafx.fxml;
    opens com.example.socialnetwork.utils.events to javafx.fxml;

    exports com.example.socialnetwork;
    exports com.example.socialnetwork.dto;
    exports com.example.socialnetwork.utils.events;
    exports com.example.socialnetwork.domain;
    exports com.example.socialnetwork.controller;
}
