package tourplanner.tourplanner.service;

import tourplanner.tourplanner.model.Tour;

import java.util.List;

public interface TourService {
    List<Tour> getAllTours();

    void addTour(Tour t);

    void removeTour(Tour t);

}
