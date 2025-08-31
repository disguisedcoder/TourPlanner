package tourplanner.tourplanner.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.File;
import java.nio.file.Path;

@RequiredArgsConstructor

@Controller
public class MenuController {
    private final MainViewModel vm;
    private final CreateTourController createTourController;

    // FILE
    @FXML
    void onNewTour() {
        createTourController.showDialog();
    }
    @FXML
    void onReportTour() {
        var sel = vm.selectedTourProperty().get();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING, "Bitte zuerst eine Tour auswählen.").showAndWait();
            return;
        }

        // Dateiauswahl (PDF)
        FileChooser fc = new FileChooser();
        fc.setTitle("Report speichern");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        String safe = sel.getNameProperty().get().trim().replaceAll("\\s+", "");
        fc.setInitialFileName("Tour_" + safe + "-report.pdf");

        File out = fc.showSaveDialog(null); // ggf. Stage referenzieren statt null
        if (out == null) return;

        Path target = out.toPath();

        // im Hintergrund erzeugen
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                vm.reportSelectedTour(target); // wirft ggf. RuntimeException/UncheckedIOException
                return null;
            }
        };

        task.setOnSucceeded(ev ->
                new Alert(Alert.AlertType.INFORMATION,
                        "Report erstellt:\n" + target.toAbsolutePath()).showAndWait());

        task.setOnFailed(ev ->
                new Alert(Alert.AlertType.ERROR,
                        "Report fehlgeschlagen:\n" +
                                (task.getException() != null ? task.getException().getMessage() : "Unbekannter Fehler"))
                        .showAndWait());

        new Thread(task, "report-tour-thread").start();
    }

//    @FXML
//    void onEditTour() {
//        TourViewModel sel = vm.selectedTourProperty().get();
//        if (sel == null) {
//            new Alert(Alert.AlertType.WARNING,
//                    "Bitte zuerst eine Tour auswählen.", ButtonType.OK)
//                    .showAndWait();
//        } else {
//            EditTourController ctrl = new EditTourController();
//            ctrl.setTour(sel);
//            ctrl.showDialog();
//        }
//    }
//
//    @FXML
//    void onDeleteTour() {
//        vm.deleteSelectedTour();
//    }
//
//    @FXML
//    void onExit() {
//        System.exit(0);
//    }
//
//    // EDIT
//    @FXML
//    void onFindTour() {
//        TextInputDialog dlg = new TextInputDialog();
//        dlg.setTitle("Find Tour");
//        dlg.setHeaderText("Teil des Tour-Namens suchen:");
//        dlg.showAndWait().ifPresent(q -> vm.findTours(q));
//    }
//
//    @FXML
//    void onReloadTours() {
//        vm.refreshTours();
//    }
//
//    // OPTIONS
//    @FXML
//    void onSettings() {
//        new Alert(Alert.AlertType.INFORMATION,
//                "Settings dialog would appear here.",
//                ButtonType.OK).showAndWait();
//    }
//
//    // HELP
//    @FXML
//    void onAbout() {
//        new Alert(Alert.AlertType.INFORMATION,
//                "TourPlannerFX v1.0\nBatuhan Saimler, Jansen Wu",
//                ButtonType.OK).showAndWait();
//    }
}
