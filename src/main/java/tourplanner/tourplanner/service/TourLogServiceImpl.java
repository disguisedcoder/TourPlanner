package tourplanner.tourplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.persistence.TourLogRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class TourLogServiceImpl implements TourLogService{
    private final TourLogRepository tourLogRepository;

    @Override
    public List<TourLog> getLogsForTour(String tourName) {
        return tourLogRepository.findAll();
    }

    @Override
    public void addLog(TourLog log) {
        tourLogRepository.save(log);
    }

    @Override
    public void removeLog(TourLog log) {
        tourLogRepository.delete(log);
    }
}
