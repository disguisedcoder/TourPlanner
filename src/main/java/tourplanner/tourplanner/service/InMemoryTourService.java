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
    public void addTour(Tour t) {
        data.add(t);
    }

    @Override
    public void removeTour(Tour t) {
        data.remove(t);
    }

}
