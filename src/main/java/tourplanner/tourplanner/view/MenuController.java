package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class MenuController {
    private final MainViewModel vm = MainViewModel.getInstance();

    // FILE
    @FXML
    void onNewTour() {
        new CreateTourController().showDialog();
    }

    @FXML
    void onEditTour() {
        TourViewModel sel = vm.selectedTourProperty().get();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte zuerst eine Tour auswählen.", ButtonType.OK)
                    .showAndWait();
        } else {
            EditTourController ctrl = new EditTourController();
            ctrl.setTour(sel);
            ctrl.showDialog();
        }
    }

    @FXML
    void onDeleteTour() {
        vm.deleteSelectedTour();
    }

    @FXML
    void onExit() {
        System.exit(0);
    }

    // EDIT
    @FXML
    void onFindTour() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Find Tour");
        dlg.setHeaderText("Teil des Tour-Namens suchen:");
        dlg.showAndWait().ifPresent(q -> vm.findTours(q));
    }

    @FXML
    void onReloadTours() {
        vm.refreshTours();
    }

    // OPTIONS
    @FXML
    void onSettings() {
        new Alert(Alert.AlertType.INFORMATION,
                "Settings dialog would appear here.",
                ButtonType.OK).showAndWait();
    }

    // HELP
    @FXML
    void onAbout() {
        new Alert(Alert.AlertType.INFORMATION,
                "TourPlannerFX v1.0\nBatuhan Saimler, Jansen Wu",
                ButtonType.OK).showAndWait();
    }
}
