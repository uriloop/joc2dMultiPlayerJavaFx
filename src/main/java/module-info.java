module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.media;


    opens com.example.demo2 to javafx.fxml, com.fasterxml.jackson.databind, javafx.media;
    exports com.example.demo2;
    exports com.example.demo2.view;
    opens com.example.demo2.view to com.fasterxml.jackson.databind, javafx.fxml, javafx.media;
    exports com.example.demo2.model;
    opens com.example.demo2.model to com.fasterxml.jackson.databind, javafx.fxml, javafx.media;
    exports com.example.demo2.conexio;
    opens com.example.demo2.conexio to com.fasterxml.jackson.databind, javafx.fxml, javafx.media;
    exports com.example.demo2.utils;
    opens com.example.demo2.utils to com.fasterxml.jackson.databind, javafx.fxml, javafx.media;

}