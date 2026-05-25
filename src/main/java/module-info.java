module com.fastbite {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.google.gson;

    opens com.fastbite to javafx.fxml;
    opens com.fastbite.model to javafx.base, com.google.gson;
    opens com.fastbite.controller to javafx.fxml;
    opens com.fastbite.persistence to com.google.gson;
    opens com.fastbite.util to javafx.fxml;
    opens com.fastbite.exception to javafx.fxml;

    exports com.fastbite;
    exports com.fastbite.model;
    exports com.fastbite.controller;
    exports com.fastbite.util;
    exports com.fastbite.persistence;
    exports com.fastbite.exception;
}