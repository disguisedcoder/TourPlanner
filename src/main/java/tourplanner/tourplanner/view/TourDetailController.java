package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.File;
import java.net.URL;

public class TourDetailController {
    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextArea  descriptionArea;
    @FXML private TextField transportField;
    @FXML private TextField estimateField;
    @FXML private ImageView mapView;

    private final MainViewModel vm = MainViewModel.getInstance();
    private       TourViewModel  lastBound;
    private static final String  FALLBACK = "/view/images/demo.png";

    @FXML
    public void initialize() {
        StackPane pane = (StackPane) mapView.getParent();
        mapView.fitWidthProperty().bind(pane.widthProperty());
        mapView.fitHeightProperty().bind(pane.heightProperty());

        vm.selectedTourProperty().addListener((obs, o, n) -> {
            if (lastBound != null) {
                nameField.textProperty().unbind();
                fromField.textProperty().unbind();
                toField.textProperty().unbind();
                distField.textProperty().unbind();
                descriptionArea.textProperty().unbind();
                transportField.textProperty().unbind();
                estimateField.textProperty().unbind();
            }
            lastBound = n;
            if (n != null) {
                nameField.textProperty().bind(n.nameProperty());
                fromField.textProperty().bind(n.fromProperty());
                toField.textProperty().bind(n.toProperty());
                distField.textProperty().bind(
                        Bindings.concat(n.distanceProperty().asString("%.1f"), " km"));
                descriptionArea.textProperty().bind(n.descriptionProperty());
                transportField.textProperty().bind(n.transportTypeProperty());
                estimateField.textProperty().bind(
                        Bindings.concat(n.estimatedTimeProperty().asString(), " min"));

                mapView.setImage(safeImage(n.imagePathProperty().get()));
                n.imagePathProperty().addListener((pObs, oldP, newP) ->
                        mapView.setImage(safeImage(newP)));
            } else {
                nameField.clear(); fromField.clear(); toField.clear(); distField.clear();
                descriptionArea.clear(); transportField.clear(); estimateField.clear();
                mapView.setImage(null);
            }
        });
    }

    private Image safeImage(String path) {
        try {
            if (path != null && !path.isBlank()) {
                if (path.contains("://"))
                    return new Image(path, true);
                File f = new File(path);
                if (f.exists())
                    return new Image(f.toURI().toString(), true);
                URL res = getClass().getResource(path.startsWith("/") ? path : "/" + path);
                if (res != null)
                    return new Image(res.toExternalForm(), true);
            }
        } catch (Exception ignored) { }
        URL res = getClass().getResource(FALLBACK);
        return new Image(res.toExternalForm(), true);
    }
}
