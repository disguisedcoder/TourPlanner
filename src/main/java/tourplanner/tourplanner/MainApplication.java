package tourplanner.tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tourplanner.tourplanner.service.InMemoryTourService;
import tourplanner.tourplanner.service.InMemoryTourLogService;
import tourplanner.tourplanner.viewmodel.MainViewModel;

public class MainApplication extends Application {
    @Override public void start(Stage stage) throws Exception {
        // VM‐Singleton initialisieren
        MainViewModel.init(
                new InMemoryTourService(),
                new InMemoryTourLogService()
        );

        // FXML laden – alle Controller brauchen nur Default‐Konstruktor
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/tourplanner/tourplanner/view/MainView.fxml")
        );
        BorderPane root = loader.load();
        stage.setScene(new Scene(root,900,600));
        stage.setTitle("TourPlannerFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
