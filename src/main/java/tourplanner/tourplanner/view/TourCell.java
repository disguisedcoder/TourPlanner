package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TourCell extends ListCell<TourViewModel> {

    @FXML private ImageView thumb;
    @FXML private Label     name;
    private static final String FALLBACK = "/view/images/demo.png";

    public TourCell() {
        try {
            FXMLLoader f = new FXMLLoader(getClass()
                    .getResource("/tourplanner/tourplanner/view/TourCell.fxml"));
            f.setController(this);
            f.load();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    protected void updateItem(TourViewModel vm, boolean empty) {
        super.updateItem(vm, empty);

        if (empty || vm == null) { setGraphic(null); return; }

        name.textProperty().bind(vm.nameProperty());
        thumb.setImage(load(vm.imagePathProperty().get()));
        setGraphic(thumb.getParent());
    }

    private Image load(String path) {
        try {
            if (path == null || path.isBlank())
                return new Image(getResourceUrl(FALLBACK), true);

            if (path.contains("://"))
                return new Image(path, true);

            File f = new File(path);
            if (f.exists())
                return new Image(f.toURI().toString(), true);

            return new Image(getResourceUrl(path), true);

        } catch (Exception e) {
            return new Image(getResourceUrl(FALLBACK), true);
        }
    }

    private String getResourceUrl(String cpPath) {
        URL u = getClass().getResource(cpPath.startsWith("/") ? cpPath : "/" + cpPath);
        if (u == null) throw new IllegalArgumentException("Resource not found: " + cpPath);
        return u.toExternalForm();
    }
}
