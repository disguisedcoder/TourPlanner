package tourplanner.tourplanner.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TourLog;

import java.util.List;

@Repository
public interface TourLogRepository extends JpaRepository<TourLog, Long> {

    List<TourLog> findByTourOrderByDateTimeDesc(Tour tour);

    List<TourLog> findByTour_NameOrderByDateTimeDesc(String tourName);

    long countByTour(Tour tour);
}
