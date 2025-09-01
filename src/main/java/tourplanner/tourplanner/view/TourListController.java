package tourplanner.tourplanner.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

@RequiredArgsConstructor

@Slf4j
@Controller
public class TourListController {
    @FXML private TextField searchField;
    @FXML private ListView<TourViewModel> tourList;
    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;

    private final MainViewModel vm;
    private final CreateTourController createTourController;
    private final EditController editController;



    @FXML public void initialize() {
        vm.loadTours();
        tourList.setItems(vm.allTours);
        tourList.setCellFactory(lv -> new TourCell());
        // selektiere immer die Property im MainViewModel
        vm.selectedTourProperty().bind(
                tourList.getSelectionModel().selectedItemProperty()
        );
        // Buttons nur aktiv, wenn eine Tour selektiert ist
        updateBtn.disableProperty().bind(vm.selectedTourProperty().isNull());
        deleteBtn.disableProperty().bind(vm.selectedTourProperty().isNull());
    }

//    @FXML void onSearch() {
//        vm.findTours(searchField.getText());
//    }

        @FXML
        public void onEditTour(ActionEvent e) {
            var sel = vm.selectedTourProperty().get();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Bitte zuerst eine Tour auswählen.").showAndWait();
                return;
            }
            editController.showDialog(sel);

        }
        @FXML void onDeleteTour(ActionEvent actionEvent) {
            var sel = vm.selectedTourProperty().get();
            if (sel == null) {
                new Alert(Alert.AlertType.WARNING, "Bitte zuerst eine Tour auswählen.").showAndWait();
                return;
            }
            var ok = new Alert(Alert.AlertType.CONFIRMATION,
                    "Tour \"" + sel.getNameProperty().get() + "\" löschen?",
                    ButtonType.OK, ButtonType.CANCEL).showAndWait();
            if (ok.isPresent() && ok.get() == ButtonType.OK) {
                vm.deleteSelectedTour();
                tourList.getSelectionModel().clearSelection();
            }
    }
    public void onNewTour(ActionEvent e) {
        createTourController.showDialog();
    }


    public void onSearch(ActionEvent actionEvent) {
        vm.searchQueryProperty().set(searchField.getText());
    }
}
