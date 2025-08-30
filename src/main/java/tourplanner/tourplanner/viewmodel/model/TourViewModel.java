package tourplanner.tourplanner.viewmodel.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import tourplanner.tourplanner.model.Tour;

@Getter
public class TourViewModel {
    private StringProperty nameProperty;
    private StringProperty  fromProperty;
    private StringProperty  toProperty;
    private StringProperty distanceProperty;
    private StringProperty  descriptionProperty;
    private StringProperty  transportTypeProperty;
    private StringProperty estimatedTimeProperty;

    public TourViewModel(StringProperty nameProperty, StringProperty fromProperty, StringProperty toProperty, StringProperty distanceProperty, StringProperty descriptionProperty, StringProperty transportTypeProperty, StringProperty estimatedTimeProperty) {
        this.nameProperty = nameProperty;
        this.fromProperty = fromProperty;
        this.toProperty = toProperty;
        this.distanceProperty = distanceProperty;
        this.descriptionProperty = descriptionProperty;
        this.transportTypeProperty = transportTypeProperty;
        this.estimatedTimeProperty = estimatedTimeProperty;
    }

    public TourViewModel(Tour tour) {
        this.nameProperty = new SimpleStringProperty(tour.getName());
        this.fromProperty = new SimpleStringProperty(tour.getFromLocation());
        this.toProperty = new SimpleStringProperty(tour.getToLocation());
        this.distanceProperty = new SimpleStringProperty(String.valueOf(tour.getDistance()));
        this.descriptionProperty = new SimpleStringProperty(tour.getDescription());
        this.transportTypeProperty = new SimpleStringProperty(tour.getTransportType());
        this.estimatedTimeProperty = new SimpleStringProperty(String.valueOf(tour.getEstimatedTime()));
    }

    public Tour toModel() {
        return new Tour(
                nameProperty.get(),
                fromProperty.get(),
                toProperty.get(),
                Double.parseDouble(distanceProperty.get()),
                descriptionProperty.get(),
                Integer.parseInt(estimatedTimeProperty.get()),
                transportTypeProperty.get()
        );
    }
}
