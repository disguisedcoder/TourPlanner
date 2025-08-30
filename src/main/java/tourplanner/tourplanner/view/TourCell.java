package tourplanner.tourplanner.view;

import javafx.scene.control.ListCell;
import lombok.extern.slf4j.Slf4j;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

@Slf4j
public class TourCell extends ListCell<TourViewModel> {

    public TourCell() {}

    @Override
    protected void updateItem(TourViewModel vm, boolean empty) {
        super.updateItem(vm, empty);

        if (empty || vm == null) { setGraphic(null); return; }

        setText(vm.getNameProperty().get());
    }
}
