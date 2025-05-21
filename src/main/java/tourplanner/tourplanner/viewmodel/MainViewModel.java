package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.service.InMemoryTourService;
import tourplanner.tourplanner.service.TourService;

public class MainViewModel {
    private final TourService service = new InMemoryTourService();
    private final ObservableList<TourViewModel> tours = FXCollections.observableArrayList();

    public MainViewModel() {
        service.getAllTours().forEach(t -> tours.add(new TourViewModel(t)));
    }

    public ObservableList<TourViewModel> getTours() {
        return tours;
    }
}
