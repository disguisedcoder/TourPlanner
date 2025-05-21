package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.service.InMemoryTourLogService;
import tourplanner.tourplanner.service.TourLogService;

import java.time.LocalDateTime;

public class TourLogViewModel {
    private final TourLogService service = new InMemoryTourLogService();
    private final ObservableList<TourLog> logs = FXCollections.observableArrayList();

    public TourLogViewModel(String tourName) {
        service.getLogsForTour(tourName).forEach(logs::add);
    }
    public ObservableList<TourLog> getLogs() { return logs; }
    public void addLog(String tourName, LocalDateTime dt,
                       String c, int diff, double dist, String rate) {
        var log = new TourLog(tourName, dt, c, diff, dist, rate);
        service.addLog(log); logs.add(log);
    }
    public void removeLog(TourLog log) {
        service.removeLog(log); logs.remove(log);
    }
}
