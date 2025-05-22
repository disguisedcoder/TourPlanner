package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourDetailController {
    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private ImageView mapView;

    private final MainViewModel vm = MainViewModel.getInstance();
    private TourViewModel lastBound;

    @FXML public void initialize() {
        vm.selectedTourProperty().addListener((obs, oldT, newT) -> {
            // Aufräumen
            if (lastBound != null) {
                nameField.textProperty().unbind();
                fromField.textProperty().unbind();
                toField.textProperty().unbind();
                distField.textProperty().unbind();
                mapView.imageProperty().unbind();
            }
            lastBound = newT;

            if (newT != null) {
                // Bind TextFields
                nameField.textProperty().bind(newT.nameProperty());
                fromField.textProperty().bind(newT.fromProperty());
                toField.textProperty().bind(newT.toProperty());
                distField.textProperty().bind(
                        Bindings.concat(
                                newT.distanceProperty().asString("%.1f"), " km"
                        )
                );
                // Bind ImageView
                mapView.imageProperty().bind(
                        Bindings.createObjectBinding(
                                () -> {
                                    String path = newT.imagePathProperty().get();
                                    return (path==null||path.isEmpty())
                                            ? null
                                            : new Image(path, true);
                                },
                                newT.imagePathProperty()
                        )
                );
            } else {
                // Keine Tour selektiert
                nameField.setText("");
                fromField.setText("");
                toField.setText("");
                distField.setText("");
                mapView.setImage(null);
            }
        });
    }
}
