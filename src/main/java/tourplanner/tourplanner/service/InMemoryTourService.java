package tourplanner.tourplanner.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.Tour;

import java.util.List;

public class InMemoryTourService implements TourService {
    private final ObservableList<Tour> data = FXCollections.observableArrayList();

    @Override
    public List<Tour> getAllTours() {
        return data;
    }

    @Override
    public void addTour(Tour tour) {
        data.add(tour);
    }

    @Override
    public void removeTour(Tour tour) {
        data.remove(tour);
    }
    @Override
    public void updateTour(Tour tour) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tour.getName())) {
                data.set(i, tour);
                return;
            }
        }
        data.add(tour);
    }
}
