module com.example.asteroids {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.asteroids.main to javafx.fxml;
    exports com.example.asteroids.main;
}