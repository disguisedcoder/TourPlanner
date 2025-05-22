package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourListController {
    @FXML private TextField searchField;
    @FXML private ListView<TourViewModel> tourList;

    private final MainViewModel vm = MainViewModel.getInstance();

    @FXML public void initialize() {
        tourList.setItems(vm.tours);
        tourList.setCellFactory(lv -> new TourCell());
        // selektiere immer die Property im MainViewModel
        vm.selectedTourProperty().bind(
                tourList.getSelectionModel().selectedItemProperty()
        );
    }

    @FXML void onSearch() {
        vm.findTours(searchField.getText());
    }

    @FXML void onEditTour() {
        TourViewModel sel = tourList.getSelectionModel().getSelectedItem();
        if (sel != null) {
            EditTourController c = new EditTourController();
            c.setTour(sel);
            c.showDialog();
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Bitte zuerst eine Tour auswählen.").showAndWait();
        }
    }

    @FXML void onDeleteTour() {
        vm.deleteSelectedTour();
    }
}
