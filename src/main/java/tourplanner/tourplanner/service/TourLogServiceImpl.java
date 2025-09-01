// src/main/java/tourplanner/tourplanner/service/TourLogServiceImpl.java
package tourplanner.tourplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.persistence.TourLogRepository;
import tourplanner.tourplanner.persistence.TourRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TourLogServiceImpl implements TourLogService {

    private final TourLogRepository tourLogRepository;
    private final TourRepository tourRepo;

    @Override
    public List<TourLog> getLogsForTour(String tourName) {
        return List.of();
    }

    @Override
    public void addLog(Tour tour, TourLog log) {
        if (tour == null || tour.getId() == 0L)
            throw new IllegalArgumentException("Tour-ID erforderlich");

        // immer MANAGED referenz verwenden
        Tour managed = tourRepo.getReferenceById(tour.getId());
        log.setTour(managed);
        if (log.getTourName() == null || log.getTourName().isBlank()) {
            log.setTourName(tour.getName());
        }
        basicValidate(log);
        tourLogRepository.save(log);
    }

    @Override
    public void updateLog(TourLog log) {
        if (log.getId() == 0L) throw new IllegalArgumentException("Log-ID erforderlich");

        if (log.getTour() != null && log.getTour().getId() != 0L) {
            log.setTour(tourRepo.getReferenceById(log.getTour().getId()));
        }

        basicValidate(log);
        tourLogRepository.save(log);
    }

    @Override
    public void removeLog(TourLog log) {
        if (log.getId() == 0L) throw new IllegalArgumentException("Log-ID needed for deletion");
        tourLogRepository.deleteById(log.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public int countLogsForTour(Tour tour) {
        return (int) tourLogRepository.countByTour(tour);
    }

    private static void basicValidate(TourLog l) {
        if (l.getDateTime() == null) throw new IllegalArgumentException("dateTime required");
        if (l.getComment() == null || l.getComment().isBlank()) throw new IllegalArgumentException("comment required");
        if (l.getDifficulty() < 1 || l.getDifficulty() > 5) throw new IllegalArgumentException("Difficulty 1..5");
        if (l.getTotalTime() < 0) throw new IllegalArgumentException("totalTime (min) ≥ 0");
        if (l.getTotalDistance() < 0) throw new IllegalArgumentException("distance (km) ≥ 0");
        if (l.getRating() < 1 || l.getRating() > 5) throw new IllegalArgumentException("Rating 1..5");
    }
}
