package tourplanner.tourplanner.service;

import tourplanner.tourplanner.model.TourLog;

import java.util.List;

public interface TourLogService {
    List<TourLog> getLogsForTour(String tourName);

    void addLog(TourLog log);

    void removeLog(TourLog log);
}
