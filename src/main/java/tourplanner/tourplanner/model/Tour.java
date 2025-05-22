package tourplanner.tourplanner.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Tour {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty from = new SimpleStringProperty("");
    private final StringProperty to = new SimpleStringProperty("");
    private final StringProperty transportType = new SimpleStringProperty("");
    private final DoubleProperty distance = new SimpleDoubleProperty(0);
    private final StringProperty estimatedTime = new SimpleStringProperty("");
    private final StringProperty imagePath = new SimpleStringProperty("");

    public Tour() {
        // Default-Konstruktor
    }

    public Tour(String name,
                String description,
                String from,
                String to,
                String transportType,
                double distance,
                String estimatedTime,
                String imagePath) {
        this.name.set(name);
        this.description.set(description);
        this.from.set(from);
        this.to.set(to);
        this.transportType.set(transportType);
        this.distance.set(distance);
        this.estimatedTime.set(estimatedTime);
        this.imagePath.set(imagePath);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty fromProperty() {
        return from;
    }

    public StringProperty toProperty() {
        return to;
    }

    public StringProperty transportTypeProperty() {
        return transportType;
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public StringProperty estimatedTimeProperty() {
        return estimatedTime;
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getFrom() {
        return from.get();
    }

    public String getTo() {
        return to.get();
    }

    public String getTransportType() {
        return transportType.get();
    }

    public double getDistance() {
        return distance.get();
    }

    public String getEstimatedTime() {
        return estimatedTime.get();
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public void setName(String v) {
        name.set(v);
    }

    public void setDescription(String v) {
        description.set(v);
    }

    public void setFrom(String v) {
        from.set(v);
    }

    public void setTo(String v) {
        to.set(v);
    }

    public void setTransportType(String v) {
        transportType.set(v);
    }

    public void setDistance(double v) {
        distance.set(v);
    }

    public void setEstimatedTime(String v) {
        estimatedTime.set(v);
    }

    public void setImagePath(String v) {
        imagePath.set(v);
    }
}
