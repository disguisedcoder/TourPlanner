package tourplanner.tourplanner.service.response;

import java.util.List;

public class Feature {
    private Geometry geometry;
    private List<Feature> features;

    public Feature(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry geometry() {
        return geometry;
    }
}