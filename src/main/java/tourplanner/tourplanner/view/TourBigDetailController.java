package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

public class TourBigDetailController {
    @FXML private ImageView mapView;

    private final MainViewModel vm = MainViewModel.getInstance();
    private TourViewModel last;

    @FXML public void initialize() {
        vm.selectedTourProperty().addListener((obs, oldSel, newSel) -> {
            // alte Bindings/Listener auf imageProperty lösen
            if (last != null) {
                mapView.imageProperty().unbind();
            }
            last = newSel;

            if (newSel != null) {
                // Bild dynamisch neu laden, wenn sich imagePath ändert
                mapView.imageProperty().bind(
                        Bindings.createObjectBinding(
                                () -> {
                                    String path = newSel.imagePathProperty().get();
                                    return (path == null || path.isEmpty())
                                            ? null
                                            : new Image(path, true);
                                },
                                newSel.imagePathProperty()
                        )
                );
            } else {
                mapView.setImage(null);
            }
        });
    }
}
