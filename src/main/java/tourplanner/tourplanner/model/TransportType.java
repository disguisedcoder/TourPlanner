package tourplanner.tourplanner.model;

public enum TransportType {

    DRIVING_CAR("Car"),
    CYCLING_REGULAR("Regular Bike"),
    FOOT_WALKING("Walking");

    private final String label;
    TransportType(String label) { this.label = label; }
    public String getLabel() { return label; }

    @Override public String toString() { return label; }
}
