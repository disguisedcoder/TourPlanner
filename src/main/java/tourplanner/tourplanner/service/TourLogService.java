package tourplanner.tourplanner.service;

import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TourLog;

import java.util.List;

public interface TourLogService {
    List<TourLog> getLogsForTour(String tourName);

    void addLog(Tour tour, TourLog log);

    void updateLog(TourLog log);

    void removeLog(TourLog log);

    int countLogsForTour(Tour tour);


}
