package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import tourplanner.tourplanner.model.Tour;

public class TourViewModel {
    private final Tour model;

    public TourViewModel(Tour model) {
        this.model = model;
        // Default-Platzhalter setzen, falls kein Bildpfad existiert
        String current = model.imagePathProperty().get();
        if (current == null || current.isBlank()) {
            String placeholder = getClass()
                    .getResource("/tourplanner/tourplanner/view/images/demo.png")
                    .toExternalForm();
            model.imagePathProperty().set(placeholder);
        }
    }

    public StringProperty nameProperty() {
        return model.nameProperty();
    }

    public StringProperty descriptionProperty() {
        return model.descriptionProperty();
    }

    public StringProperty fromProperty() {
        return model.fromProperty();
    }

    public StringProperty toProperty() {
        return model.toProperty();
    }

    public StringProperty transportTypeProperty() {
        return model.transportTypeProperty();
    }

    public javafx.beans.property.DoubleProperty distanceProperty() {
        return model.distanceProperty();
    }

    public StringProperty estimatedTimeProperty() {
        return model.estimatedTimeProperty();
    }

    public StringProperty imagePathProperty() {
        return model.imagePathProperty();
    }

    public boolean isValid() {
        return !nameProperty().get().isEmpty()
                && !fromProperty().get().isEmpty()
                && !toProperty().get().isEmpty();
    }

    public Image getMapImage() {
        return new Image(imagePathProperty().get(), true);
    }

    public Tour toModel() {
        return new Tour(
                nameProperty().get(),
                descriptionProperty().get(),
                fromProperty().get(),
                toProperty().get(),
                transportTypeProperty().get(),
                distanceProperty().get(),
                estimatedTimeProperty().get(),
                imagePathProperty().get()
        );
    }
}
