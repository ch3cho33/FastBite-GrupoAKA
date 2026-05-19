module com.fastbite.fastbite {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.fastbite.fastbite to javafx.fxml;
    opens com.fastbite.fastbite.model to com.google.gson;

    exports com.fastbite.fastbite;
}