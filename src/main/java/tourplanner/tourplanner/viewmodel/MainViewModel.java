package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import tourplanner.tourplanner.service.InMemoryTourService;
import tourplanner.tourplanner.service.TourService;

public class MainViewModel {
    private final TourService service = new InMemoryTourService();
    private final ObservableList<TourViewModel> allTours =
            FXCollections.observableArrayList();
    private final FilteredList<TourViewModel> tours =
            new FilteredList<>(allTours, t -> true);

    public MainViewModel() {
        service.getAllTours().forEach(t -> allTours.add(new TourViewModel(t)));
    }

    public FilteredList<TourViewModel> getTours() {
        return tours;
    }

    public void addTour(TourViewModel vm) {
        service.addTour(vm.toModel());
        allTours.add(vm);
    }

    public void removeTour(TourViewModel vm) {
        service.removeTour(vm.toModel());
        allTours.remove(vm);
    }

    public void editTour(TourViewModel vm) { /* siehe vorher */ }

    public void filterTours(String q) {
        tours.setPredicate(t -> t.nameProperty().get()
                .toLowerCase().contains(q.toLowerCase()));
    }

    public void reloadTours() {
        allTours.clear();
        service.getAllTours().forEach(t -> allTours.add(new TourViewModel(t)));
        tours.setPredicate(t -> true);
    }
}
