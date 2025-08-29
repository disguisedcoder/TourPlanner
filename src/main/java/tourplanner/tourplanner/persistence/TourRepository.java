package tourplanner.tourplanner.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tourplanner.tourplanner.model.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
}
