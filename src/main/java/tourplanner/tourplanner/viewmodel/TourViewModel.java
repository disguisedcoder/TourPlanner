package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.*;
import tourplanner.tourplanner.model.Tour;

public class TourViewModel {
    private final Tour model;

    public TourViewModel(Tour model) {
        this.model = model;
    }

    public StringProperty  nameProperty()          { return model.nameProperty(); }
    public StringProperty  fromProperty()          { return model.fromProperty(); }
    public StringProperty  toProperty()            { return model.toProperty(); }
    public DoubleProperty  distanceProperty()      { return model.distanceProperty(); }
    public StringProperty  imagePathProperty()     { return model.imagePathProperty(); }
    public StringProperty  descriptionProperty()   { return model.descriptionProperty(); }
    public StringProperty  transportTypeProperty() { return model.transportTypeProperty(); }
    public IntegerProperty estimatedTimeProperty() { return model.estimatedTimeProperty(); }

    public boolean isValid() {
        return !nameProperty().get().isEmpty()
                && !fromProperty().get().isEmpty()
                && !toProperty().get().isEmpty();
    }

    public Tour toModel() {
        return new Tour(
                nameProperty().get(),
                fromProperty().get(),
                toProperty().get(),
                distanceProperty().get(),
                estimatedTimeProperty().get(),
                transportTypeProperty().get(),
                descriptionProperty().get(),
                imagePathProperty().get()
        );
    }
}
