package tourplanner.tourplanner.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    private final MainViewModel vm;

    @FXML public void initialize() {
        vm.loadTours();
        tourList.setItems(vm.allTours);
        tourList.setCellFactory(lv -> new TourCell());
        // selektiere immer die Property im MainViewModel
        vm.selectedTourProperty().bind(
                tourList.getSelectionModel().selectedItemProperty()
        );
    }

//    @FXML void onSearch() {
//        vm.findTours(searchField.getText());
//    }
//
//    @FXML void onEditTour() {
//        TourViewModel sel = tourList.getSelectionModel().getSelectedItem();
//        if (sel != null) {
//            EditTourController c = new EditTourController();
//            c.setTour(sel);
//            c.showDialog();
//        } else {
//            new Alert(Alert.AlertType.WARNING,
//                    "Bitte zuerst eine Tour auswählen.").showAndWait();
//        }
//    }
//
//    @FXML void onDeleteTour() {
//        vm.deleteSelectedTour();
//    }
}
