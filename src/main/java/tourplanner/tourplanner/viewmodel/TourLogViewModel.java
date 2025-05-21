package tourplanner.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.service.InMemoryTourLogService;
import tourplanner.tourplanner.service.TourLogService;

public class TourLogViewModel {
    private final TourLogService service = new InMemoryTourLogService();
    private final ObservableList<TourLog> logs = FXCollections.observableArrayList();

    public TourLogViewModel(String tourName) {
        service.getLogsForTour(tourName).forEach(logs::add);
    }

    public ObservableList<TourLog> getLogs() {
        return logs;
    }
}
