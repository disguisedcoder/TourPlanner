module tourplanner.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens tourplanner.tourplanner to javafx.fxml;
    exports tourplanner.tourplanner;
    exports tourplanner.tourplanner.view;
    opens tourplanner.tourplanner.app to javafx.fxml;
    opens tourplanner.tourplanner.view to javafx.fxml;
}