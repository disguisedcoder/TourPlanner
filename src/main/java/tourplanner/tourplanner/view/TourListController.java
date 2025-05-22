package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourListController {
    @FXML private ListView<TourViewModel> tourListView;

    private final MainViewModel vm = MainViewModel.getInstance();

    @FXML public void initialize() {
        tourListView.setItems(vm.tours);
        tourListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV) -> vm.selectedTourProperty().set(newV));
    }

    @FXML private void onCreateTour() {
        new CreateTourController().showDialog();
    }

    @FXML private void onEditTour() {
        TourViewModel sel = tourListView.getSelectionModel().getSelectedItem();
        if (sel != null) {
            // Hier wurde vorher setTour(...) verwendet – jetzt korrekt showDialog(...)
            new EditTourController().showDialog(sel);
        }
    }

    @FXML private void onDeleteTour() {
        vm.deleteSelectedTour();
    }

    @FXML private void onRefresh() {
        vm.refreshTours();
    }
}
