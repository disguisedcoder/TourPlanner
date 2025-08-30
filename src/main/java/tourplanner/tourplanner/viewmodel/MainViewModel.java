package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tourplanner.tourplanner.service.TourLogService;
import tourplanner.tourplanner.service.TourService;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
@Component
public class MainViewModel {

//    public static void init(TourService ts, TourLogService ls) {
//        INSTANCE = new MainViewModel(ts, ls);
//    }

    private final TourService tourSvc;
    private final TourLogService logSvc;
//    public TourLogService getLogService() { return logSvc; }

    public final ObservableList<TourViewModel> allTours = FXCollections.observableArrayList();

    public final FilteredList<TourViewModel> tours = new FilteredList<>(allTours, t -> true);

    private final ObjectProperty<TourViewModel> selectedTour =
            new SimpleObjectProperty<>();

//    private MainViewModel(TourService tourSvc, TourLogService logSvc) {
//        this.tourSvc = tourSvc;
//        this.logSvc  = logSvc;
//        tourSvc.getAllTours().forEach(t -> allTours.add(new TourViewModel(t)));
//    }
//
    public void addTour(TourViewModel tvm) {
        tourSvc.addTour(tvm.toModel());
        allTours.add(tvm);
    }

    public void loadTours() {
        allTours.clear();
        allTours.addAll(tourSvc.getAllTours().stream().map(TourViewModel::new).toList());
    }
//    public void deleteSelectedTour() {
//        var t = selectedTour.get();
//        if (t!=null) {
//            tourSvc.removeTour(t.toModel());
//            allTours.remove(t);
//        }
//    }
//    public void findTours(String query) {
//        String lower = query == null ? "" : query.toLowerCase();
//        tours.setPredicate(tv ->
//                tv.nameProperty().get().toLowerCase().contains(lower));
//    }
//
//    public void refreshTours() {
//        allTours.clear();
//        tourSvc.getAllTours()
//                .forEach(t -> allTours.add(new TourViewModel(t)));
//        tours.setPredicate(tv -> true);
//    }
//
    public ObjectProperty<TourViewModel> selectedTourProperty() {
        return selectedTour;
    }
}
