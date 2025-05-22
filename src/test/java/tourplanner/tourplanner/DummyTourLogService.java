package tourplanner.tourplanner;

import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.service.TourLogService;

import java.util.ArrayList;
import java.util.List;

class DummyTourLogService implements TourLogService {
    private final List<TourLog> data = new ArrayList<>();

    @Override
    public void addLog(TourLog l) {
        data.add(l);
    }

    @Override
    public void removeLog(TourLog l) {
        data.remove(l);
    }

    @Override
    public List<TourLog> getLogsForTour(String n) {
        return new ArrayList<>(data);
    }
}
