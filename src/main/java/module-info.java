module tourplanner.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens tourplanner.tourplanner to javafx.fxml;
    exports tourplanner.tourplanner;
    exports tourplanner.tourplanner.app;
    opens tourplanner.tourplanner.app to javafx.fxml;
}