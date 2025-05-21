package tourplanner.tourplanner.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tourplanner.tourplanner.model.TourLog;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTourLogService implements TourLogService {
    private final ObservableList<TourLog> data = FXCollections.observableArrayList();

    @Override
    public List<TourLog> getLogsForTour(String name) {
        return data.stream().filter(l -> l.tourNameProperty().get().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public void addLog(TourLog log) {
        data.add(log);
    }

    @Override
    public void removeLog(TourLog log) {
        data.remove(log);
    }
}
