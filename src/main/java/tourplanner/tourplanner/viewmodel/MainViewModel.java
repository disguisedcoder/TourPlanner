package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.ReportService;
import tourplanner.tourplanner.service.TourLogService;
import tourplanner.tourplanner.service.TourService;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ReportService reportService;
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
        loadTours();
    }

    public void loadTours() {
        allTours.clear();
        allTours.addAll(tourSvc.getAllTours().stream().map(TourViewModel::new).toList());
    }

//    public String generateMapHtml(String routeJson) {
//        if(routeJson == null || routeJson.isBlank()) {
//            return "<html><body><p>No map available</p></body></html>";
//        }
//
//        try {
//            URL url = getClass().getResource("/leaflet.html");
//            if(url == null) throw new IOException("leaflet.html not found in resources.");
//
//            String content = Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);
//            return content.replace("{MY_DIRECTIONS}", routeJson);
//        } catch(IOException | URISyntaxException e) {
//            e.printStackTrace();
//            return "<html><body><p>Error loading map</p></body></html>";
//        }
//    }

    public void reportSelectedTour(Path target) {
        var sel = selectedTour.get();
        if (sel == null) {
            throw new IllegalStateException("Bitte zuerst eine Tour auswählen.");
        }

        var tour = sel.toModel();

        try {
            reportService.generateTourReport(tour, target);
            log.info("Report erstellt: {}", target);
        } catch (IOException e) {
            log.error("Report fehlgeschlagen", e);
            throw new UncheckedIOException("Report konnte nicht erstellt werden: " + e.getMessage(), e);
        }
    }
    public void deleteSelectedTour() {
        var t = selectedTour.get();

        if (t!=null) {
            Tour temp = new Tour();
            temp.setId(t.getId());
            tourSvc.removeTour(temp);
            allTours.remove(t);
            loadTours();
        }
    }
    public void updateTour(TourViewModel tvm) {
        // tvm enthält geänderte Properties und (wichtig) die ID
        tourSvc.updateTour(tvm.toModel());  // JPA: save(...) macht Update wenn ID vorhanden
        // Kein loadTours() nötig, weil wir das vorhandene ViewModel bereits aktualisiert haben.
        // Wenn du sicherheitshalber neu laden willst:
        // loadTours();
    }

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
