package tourplanner.tourplanner.model;

import javafx.beans.property.*;

public class Tour {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final DoubleProperty distance = new SimpleDoubleProperty();
    private final StringProperty imagePath = new SimpleStringProperty();

    public Tour(String name, String from, String to, double distance, String imagePath) {
        this.name.set(name);
        this.from.set(from);
        this.to.set(to);
        this.distance.set(distance);
        this.imagePath.set(imagePath);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty fromProperty() {
        return from;
    }

    public StringProperty toProperty() {
        return to;
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    @Override
    public String toString() {
        return name.get() + " (" + distance.get() + " km)";
    }

}
