package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourSmallDetailController {
    @FXML private Label nameLbl, fromLbl, toLbl, distLbl;

    private final MainViewModel vm = MainViewModel.getInstance();
    private TourViewModel last;

    @FXML public void initialize() {
        vm.selectedTourProperty().addListener((obs, oldSel, newSel) -> {
            // alte Bindings lösen
            if (last != null) {
                nameLbl.textProperty().unbind();
                fromLbl.textProperty().unbind();
                toLbl.textProperty().unbind();
                distLbl.textProperty().unbind();
            }
            last = newSel;

            if (newSel != null) {
                // direkt an die Properties binden
                nameLbl.textProperty().bind(newSel.nameProperty());
                fromLbl.textProperty().bind(newSel.fromProperty());
                toLbl.textProperty().bind(newSel.toProperty());
                // Distanz als String mit " km"
                distLbl.textProperty().bind(
                        Bindings.concat(
                                newSel.distanceProperty().asString("%.1f"),
                                " km"
                        )
                );
            } else {
                // kein Tour ausgewählt
                nameLbl.setText("");
                fromLbl.setText("");
                toLbl.setText("");
                distLbl.setText("");
            }
        });
    }
}
