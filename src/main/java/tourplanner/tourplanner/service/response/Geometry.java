package tourplanner.tourplanner.service.response;

import java.util.List;

public class Geometry {
    private List<Double> coordinates;

    public Geometry(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Double> coordinates() {
        return coordinates;
    }
}