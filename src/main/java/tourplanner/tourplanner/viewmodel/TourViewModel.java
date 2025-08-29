package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.StringProperty;
import lombok.Getter;
import tourplanner.tourplanner.model.Tour;

@Getter
public class TourViewModel {
    private StringProperty nameProperty;
    private StringProperty  fromProperty;
    private StringProperty  toProperty;
    private StringProperty distanceProperty;
    private StringProperty  imagePathProperty;
    private StringProperty  descriptionProperty;
    private StringProperty  transportTypeProperty;
    private StringProperty estimatedTimeProperty;

    public TourViewModel(StringProperty nameProperty, StringProperty fromProperty, StringProperty toProperty, StringProperty distanceProperty, StringProperty imagePathProperty, StringProperty descriptionProperty, StringProperty transportTypeProperty, StringProperty estimatedTimeProperty) {
        this.nameProperty = nameProperty;
        this.fromProperty = fromProperty;
        this.toProperty = toProperty;
        this.distanceProperty = distanceProperty;
        this.imagePathProperty = imagePathProperty;
        this.descriptionProperty = descriptionProperty;
        this.transportTypeProperty = transportTypeProperty;
        this.estimatedTimeProperty = estimatedTimeProperty;
    }

    public Tour toModel() {
        return new Tour(
                nameProperty.get(),
                fromProperty.get(),
                toProperty.get(),
                Double.parseDouble(distanceProperty.get()),
                descriptionProperty.get(),
                Integer.parseInt(estimatedTimeProperty.get()),
                transportTypeProperty.get(),
                imagePathProperty.get().isBlank() ? "/tourplanner/tourplanner/view/images/demo.png" : imagePathProperty.get()
        );
    }
}
