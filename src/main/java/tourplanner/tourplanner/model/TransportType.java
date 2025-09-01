package tourplanner.tourplanner.model;

public enum TransportType {

    DRIVING_CAR("Car", "driving-car"),
    CYCLING_REGULAR("Regular Bike", "cycling-regular"),
    FOOT_WALKING("Walking", "foot-walking");

    private final String label;
    private final String orsProfile;

    TransportType(String label, String orsProfile) {
        this.label = label;
        this.orsProfile = orsProfile;
    }
    public String getLabel() { return label; }
    public String getOrsProfile() { return orsProfile; }

    @Override public String toString() { return label; }
}
