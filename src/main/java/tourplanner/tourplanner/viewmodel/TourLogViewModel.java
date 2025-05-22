package tourplanner.tourplanner.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.service.TourLogService;

import java.time.LocalDateTime;

public class TourLogViewModel {
    private final TourLogService service;
    private final ObservableList<TourLog> logs = FXCollections.observableArrayList();

    public TourLogViewModel(TourLogService svc, String tourName) {
        this.service = svc;
        svc.getLogsForTour(tourName).forEach(logs::add);
    }

    public ObservableList<TourLog> getLogs() { return logs; }

    public void addLog(String tourName, String comment,
                       int difficulty, double dist, String rating) {
        addLog(tourName, comment, difficulty, dist, 0, rating);
    }

    public void addLog(String tourName, String comment,
                       int difficulty, double dist, int totalTime, String rating) {
        TourLog log = new TourLog(tourName, LocalDateTime.now(), comment,
                difficulty, dist, totalTime, rating);
        service.addLog(log);
        logs.add(log);
    }

    public void removeLog(TourLog log) {
        service.removeLog(log);
        logs.remove(log);
    }
}
