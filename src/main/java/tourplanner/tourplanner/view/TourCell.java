package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.IOException;

public class TourCell extends ListCell<TourViewModel> {
    @FXML
    private ImageView thumb;
    @FXML
    private Label name;

    public TourCell() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/tourplanner/tourplanner/view/TourCell.fxml")
        );
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(TourViewModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            var p = item.imagePathProperty().get();
            thumb.setImage((p != null && !p.isEmpty())
                    ? new Image(p, true) : null);
            name.textProperty().bind(item.nameProperty());
            setGraphic(thumb.getParent());
        }
    }
}
