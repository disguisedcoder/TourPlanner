package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.*;
import tourplanner.tourplanner.model.Tour;

public class TourViewModel {
    private final Tour model;

    public TourViewModel(Tour m) {
        this.model = m;
    }

    public StringProperty nameProperty() {
        return model.nameProperty();
    }

    public StringProperty fromProperty() {
        return model.fromProperty();
    }

    public StringProperty toProperty() {
        return model.toProperty();
    }

    public DoubleProperty distanceProperty() {
        return model.distanceProperty();
    }

    public StringProperty imagePathProperty() {
        return model.imagePathProperty();
    }

    public boolean isValid() {
        return !nameProperty().get().isEmpty()
                && !fromProperty().get().isEmpty()
                && !toProperty().get().isEmpty();
    }
}
