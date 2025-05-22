package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourDetailController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField fromField;
    @FXML
    private TextField toField;
    @FXML
    private TextField transportField;
    @FXML
    private TextField distField;
    @FXML
    private TextField timeField;
    @FXML
    private ImageView mapView;
    @FXML
    private Label noImageLabel;

    private final MainViewModel vm = MainViewModel.getInstance();
    private TourViewModel lastBound;

    @FXML
    public void initialize() {
        vm.selectedTourProperty().addListener((obs, oldT, newT) -> {
            if (lastBound != null) {
                // alte Bindings lösen
                nameField.textProperty().unbind();
                descriptionField.textProperty().unbind();
                fromField.textProperty().unbind();
                toField.textProperty().unbind();
                transportField.textProperty().unbind();
                distField.textProperty().unbind();
                timeField.textProperty().unbind();
                mapView.imageProperty().unbind();
                noImageLabel.visibleProperty().unbind();
            }
            lastBound = newT;

            if (newT != null) {
                // Text-Felder binden
                nameField.textProperty().bind(newT.nameProperty());
                descriptionField.textProperty().bind(newT.descriptionProperty());
                fromField.textProperty().bind(newT.fromProperty());
                toField.textProperty().bind(newT.toProperty());
                transportField.textProperty().bind(newT.transportTypeProperty());
                distField.textProperty().bind(
                        Bindings.concat(newT.distanceProperty().asString("%.1f"), " km")
                );
                timeField.textProperty().bind(newT.estimatedTimeProperty());

                // Bild über URL laden und bei Fehlern null → Label sichtbar
                mapView.imageProperty().bind(
                        Bindings.createObjectBinding(() -> {
                            String path = newT.imagePathProperty().get();
                            if (path == null || path.isBlank()) return null;
                            try {
                                return new Image(path, true);
                            } catch (Exception ex) {
                                return null;
                            }
                        }, newT.imagePathProperty())
                );
                noImageLabel.visibleProperty().bind(mapView.imageProperty().isNull());

            } else {
                // reset
                nameField.clear();
                descriptionField.clear();
                fromField.clear();
                toField.clear();
                transportField.clear();
                distField.clear();
                timeField.clear();
                mapView.setImage(null);
                noImageLabel.setVisible(false);
            }
        });
    }
}
