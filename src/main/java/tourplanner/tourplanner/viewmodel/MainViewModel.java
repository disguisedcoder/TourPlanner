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
import tourplanner.tourplanner.view.ManageLogsController;
import tourplanner.tourplanner.viewmodel.model.TourLogViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.DoubleSummaryStatistics;
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
    private ManageLogsController manageLogsController;
//    public TourLogService getLogService() { return logSvc; }

    public final ObservableList<TourViewModel> allTours = FXCollections.observableArrayList();
    public final FilteredList<TourViewModel> tours = new FilteredList<>(allTours, t -> true);

    public final ObservableList<TourLogViewModel> allLogs = FXCollections.observableArrayList();
    public final FilteredList<TourLogViewModel> logs = new FilteredList<>(allLogs, l -> true);

    private final ObjectProperty<TourViewModel> selectedTour =
            new SimpleObjectProperty<>();
    private final StringProperty searchQuery = new SimpleStringProperty("");

    // ---- Tours ----
    public void addTour(TourViewModel tvm) {
        tourSvc.addTour(tvm.toModel());
        allTours.add(tvm);
        loadTours();
    }

    public void loadTours() {
        allTours.clear();
        allTours.addAll(tourSvc.getAllTours().stream().map(TourViewModel::new).toList());
    }
    {
        selectedTour.addListener((obs, o, n) -> loadLogsForSelected());
        searchQuery.addListener((obs, o, n) -> applySearch());
    }

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
        tourSvc.updateTour(tvm.toModel());
        loadTours();
    }

    // ---- Logs ----
    public void loadLogsForSelected() {
        allLogs.clear();
        var sel = selectedTour.get();
        if (sel == null) return;
        var tour = sel.toModel();
        allLogs.addAll(logSvc.getLogsForTour(tour.getName()).stream().map(TourLogViewModel::new).toList());
        applySearch();
    }

    public void addLog(TourLogViewModel lvm) {
        var sel = selectedTour.get();
        if (sel == null) throw new IllegalStateException("Bitte zuerst eine Tour auswählen.");
        var tour = sel.toModel();
        logSvc.addLog(tour, lvm.toModel(tour));
        loadLogsForSelected();
    }

    public void updateLog(TourLogViewModel lvm) {
        var sel = selectedTour.get();
        if (sel == null) throw new IllegalStateException("Bitte zuerst eine Tour auswählen.");
        logSvc.updateLog(lvm.toModel(sel.toModel()));
        loadLogsForSelected();
    }

    public void deleteLog(TourLogViewModel lvm) {
        if (lvm == null) return;
        logSvc.removeLog(lvm.toModel(selectedTour.get() == null ? null : selectedTour.get().toModel()));
        loadLogsForSelected();
    }
    // ---- Suche + computed attributes ----
    public void applySearch() {
        final String q = (searchQuery.get() == null ? "" : searchQuery.get().toLowerCase().trim());

        tours.setPredicate(tv -> {
            if (q.isBlank()) return true;

            // computed attributes
            int popularity = computePopularity(tv);
            int childFriendly = computeChildFriendliness(tv);

            String hay = String.join(" ",
                    nz(tv.getNameProperty().get()),
                    nz(tv.getFromProperty().get()),
                    nz(tv.getToProperty().get()),
                    nz(tv.getDescriptionProperty().get()),
                    tv.getTransportTypeProperty().get() == null ? "" : tv.getTransportTypeProperty().get().getLabel(),
                    String.valueOf(popularity),
                    String.valueOf(childFriendly)
            ).toLowerCase();
            return hay.contains(q);
        });

        logs.setPredicate(lv -> {
            if (q.isBlank()) return true;
            String hay = String.join(" ",
                    nz(lv.getUsernameProperty().get()),
                    nz(lv.getCommentProperty().get()),
                    String.valueOf(lv.getDifficultyProperty().get()),
                    String.valueOf(lv.getTotalTimeProperty().get()),
                    String.valueOf(lv.getTotalDistanceProperty().get()),
                    String.valueOf(lv.getRatingProperty().get()),
                    String.valueOf(lv.getDateProperty().get())
            ).toLowerCase();
            return hay.contains(q);
        });
    }

    private int computePopularity(TourViewModel tv) {
        try { return logSvc.countLogsForTour(tv.toModel()); }
        catch (Exception e) { return 0; }
    }


    private int computeChildFriendliness(TourViewModel tv) {
        try {
            var tour = tv.toModel();
            var logs = logSvc.getLogsForTour(tour.getName());
            if (logs.isEmpty()) return 3; // neutral ohne Daten

            double avgDiff = logs.stream().mapToInt(l -> l.getDifficulty()).average().orElse(3);
            DoubleSummaryStatistics distStat = logs.stream().mapToDouble(l -> l.getTotalDistance()).summaryStatistics();
            DoubleSummaryStatistics timeStat = logs.stream().mapToDouble(l -> l.getTotalTime()).summaryStatistics();

            double score = 5.0;
            score -= Math.max(0, (avgDiff - 1.0)) * 0.8;

            double avgDist = distStat.getAverage();
            if (avgDist > 80) score -= 2;
            else if (avgDist > 50) score -= 1;

            double avgMin = timeStat.getAverage();
            if (avgMin > 360) score -= 2;
            else if (avgMin > 240) score -= 1;

            int s = (int) Math.round(score);
            return Math.max(1, Math.min(5, s));
        } catch (Exception e) {
            return 3;
        }
    }
    // Null-Safety-Helper

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    public ObjectProperty<TourViewModel> selectedTourProperty() {
        return selectedTour;
    }
    public StringProperty searchQueryProperty() { return searchQuery; }

}
