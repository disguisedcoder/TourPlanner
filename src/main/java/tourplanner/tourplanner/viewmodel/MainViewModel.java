package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.TourService;
import tourplanner.tourplanner.service.TourLogService;

public class MainViewModel {
    private static MainViewModel INSTANCE;

    public static void init(TourService ts, TourLogService ls) {
        INSTANCE = new MainViewModel(ts, ls);
    }
    public static MainViewModel getInstance() {
        return INSTANCE;
    }

    private final TourService tourSvc;
    private final TourLogService logSvc;
    public TourLogService getLogService() { return logSvc; }

    private final ObservableList<TourViewModel> allTours =
            FXCollections.observableArrayList();
    public final FilteredList<TourViewModel> tours =
            new FilteredList<>(allTours, t -> true);

    private final ObjectProperty<TourViewModel> selectedTour =
            new SimpleObjectProperty<>();

    private MainViewModel(TourService tourSvc, TourLogService logSvc) {
        this.tourSvc = tourSvc;
        this.logSvc  = logSvc;
        tourSvc.getAllTours().forEach(t -> allTours.add(new TourViewModel(t)));
    }

    public void addTour(TourViewModel tvm) {
        tourSvc.addTour(tvm.toModel());
        allTours.add(tvm);
    }
    public void deleteSelectedTour() {
        var t = selectedTour.get();
        if (t!=null) {
            tourSvc.removeTour(t.toModel());
            allTours.remove(t);
        }
    }
    public void findTours(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        tours.setPredicate(tv ->
                tv.nameProperty().get().toLowerCase().contains(lower));
    }

    public void refreshTours() {
        allTours.clear();
        tourSvc.getAllTours()
                .forEach(t -> allTours.add(new TourViewModel(t)));
        tours.setPredicate(tv -> true);
    }

    public ObjectProperty<TourViewModel> selectedTourProperty() {
        return selectedTour;
    }
}
