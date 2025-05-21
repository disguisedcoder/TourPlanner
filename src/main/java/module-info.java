module tourplanner.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens tourplanner.tourplanner to javafx.fxml;

    opens tourplanner.tourplanner.view to javafx.fxml;

    exports tourplanner.tourplanner;
    exports tourplanner.tourplanner.view;
    exports tourplanner.tourplanner.service;
    exports tourplanner.tourplanner.model;
    exports tourplanner.tourplanner.viewmodel;
}