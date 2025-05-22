package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.service.TourLogService;

import java.time.LocalDateTime;

public class TourLogViewModel {
    private final TourLogService logSvc;
    private final ObservableList<TourLog> logs = FXCollections.observableArrayList();

    public TourLogViewModel(TourLogService logSvc, String tourName) {
        this.logSvc = logSvc;
        logSvc.getLogsForTour(tourName).forEach(logs::add);
    }

    public ObservableList<TourLog> getLogs() {
        return logs;
    }

    public void addLog(String tourName, String comment,
                       int difficulty, double dist, String rating) {
        TourLog log = new TourLog(tourName, LocalDateTime.now(),
                comment, difficulty, dist, rating);
        logSvc.addLog(log);
        logs.add(log);
    }

    public void removeLog(TourLog log) {
        logSvc.removeLog(log);
        logs.remove(log);
    }
}
