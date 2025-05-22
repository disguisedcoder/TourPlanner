package tourplanner.tourplanner;

import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.TourService;

import java.util.ArrayList;
import java.util.List;

class DummyTourService implements TourService {
    private final List<Tour> data = new ArrayList<>();

    @Override
    public List<Tour> getAllTours() {
        return data;
    }

    @Override
    public void addTour(Tour t) {
        data.add(t);
    }

    @Override
    public void removeTour(Tour t) {
        data.remove(t);
    }
}