module com.edusnake {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.edusnake to javafx.fxml;
    exports com.edusnake;
}
