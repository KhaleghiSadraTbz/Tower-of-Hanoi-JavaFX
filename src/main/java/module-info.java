module com.example.hanoitowers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;

    opens com.example.hanoitowers to javafx.fxml;
    exports com.example.hanoitowers;
}