package tourplanner.tourplanner.model;

import javafx.beans.property.*;

public class Tour {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to   = new SimpleStringProperty();
    private final DoubleProperty distance = new SimpleDoubleProperty();
    private final StringProperty imagePath = new SimpleStringProperty();

    private final StringProperty  description   = new SimpleStringProperty();
    private final StringProperty  transportType = new SimpleStringProperty();
    private final IntegerProperty estimatedTime = new SimpleIntegerProperty();

    public Tour(String name, String from, String to, double distance, String imagePath) {
        this(name, from, to, distance, 0, "", "", imagePath);
    }

    public Tour(String name, String from, String to, double distance,
                int estimatedTime, String transportType,
                String description, String imagePath) {
        this.name.set(name);
        this.from.set(from);
        this.to.set(to);
        this.distance.set(distance);
        this.estimatedTime.set(estimatedTime);
        this.transportType.set(transportType);
        this.description.set(description);
        this.imagePath.set(imagePath);
    }

    public StringProperty  nameProperty()          { return name; }
    public StringProperty  fromProperty()          { return from; }
    public StringProperty  toProperty()            { return to; }
    public DoubleProperty  distanceProperty()      { return distance; }
    public StringProperty  imagePathProperty()     { return imagePath; }
    public StringProperty  descriptionProperty()   { return description; }
    public StringProperty  transportTypeProperty() { return transportType; }
    public IntegerProperty estimatedTimeProperty() { return estimatedTime; }

    public String  getName()          { return name.get(); }
    public void    setName(String v)  { name.set(v); }

    public String  getFrom()          { return from.get(); }
    public void    setFrom(String v)  { from.set(v); }

    public String  getTo()            { return to.get(); }
    public void    setTo(String v)    { to.set(v); }

    public double  getDistance()      { return distance.get(); }
    public void    setDistance(double v) { distance.set(v); }

    public String  getImagePath()     { return imagePath.get(); }
    public void    setImagePath(String v){ imagePath.set(v); }

    public String  getDescription()   { return description.get(); }
    public void    setDescription(String v){ description.set(v); }

    public String  getTransportType() { return transportType.get(); }
    public void    setTransportType(String v){ transportType.set(v); }

    public int     getEstimatedTime() { return estimatedTime.get(); }
    public void    setEstimatedTime(int v){ estimatedTime.set(v); }

    @Override
    public String toString() {
        return name.get() + " (" + distance.get() + " km)";
    }
}
