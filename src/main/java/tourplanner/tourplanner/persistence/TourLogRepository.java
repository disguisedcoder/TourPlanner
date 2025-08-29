package tourplanner.tourplanner.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tourplanner.tourplanner.model.TourLog;

@Repository
public interface TourLogRepository extends JpaRepository<TourLog, Long> {
}
