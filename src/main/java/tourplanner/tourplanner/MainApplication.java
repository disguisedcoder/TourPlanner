package tourplanner.tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/tourplanner/tourplanner/MainView.fxml"))
        );
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("SWEN2 TourPlanner");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}