package tourplanner.tourplanner.viewmodel.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TransportType;

@Getter
public class TourViewModel {
    private Long id;
    private StringProperty nameProperty;
    private StringProperty fromProperty;
    private StringProperty toProperty;
    private StringProperty distanceProperty;
    private StringProperty descriptionProperty;
    private StringProperty estimatedTimeProperty;
    private StringProperty fromLatProperty;
    private StringProperty fromLngProperty;
    private StringProperty toLatProperty;
    private StringProperty toLngProperty;
    private final ObjectProperty<TransportType> transportTypeProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();


    public TourViewModel(StringProperty nameProperty, StringProperty fromProperty, StringProperty toProperty, StringProperty distanceProperty, StringProperty descriptionProperty, ObjectProperty<TransportType> transportTypeProperty, StringProperty estimatedTimeProperty) {
        this.nameProperty = nameProperty;
        this.fromProperty = fromProperty;
        this.toProperty = toProperty;
        this.distanceProperty = distanceProperty;
        this.descriptionProperty = descriptionProperty;
        this.transportTypeProperty.bind(transportTypeProperty);
        this.estimatedTimeProperty = estimatedTimeProperty;
    }

    public TourViewModel(Tour tour) {
        this.id = tour.getId();
        this.nameProperty = new SimpleStringProperty(tour.getName());
        this.fromProperty = new SimpleStringProperty(tour.getFromLocation());
        this.toProperty = new SimpleStringProperty(tour.getToLocation());
        this.distanceProperty = new SimpleStringProperty(String.valueOf(tour.getDistance()));
        this.descriptionProperty = new SimpleStringProperty(tour.getDescription());
        this.transportTypeProperty.set(tour.getTransportType());
        this.estimatedTimeProperty = new SimpleStringProperty(String.valueOf(tour.getEstimatedTime()));
        this.fromLatProperty = new SimpleStringProperty(String.valueOf(tour.getFromLat()));
        this.fromLngProperty = new SimpleStringProperty(String.valueOf(tour.getFromLng()));
        this.toLatProperty = new SimpleStringProperty(String.valueOf(tour.getToLat()));
        this.toLngProperty = new SimpleStringProperty(String.valueOf(tour.getToLng()));
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

