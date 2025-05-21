package tourplanner.tourplanner.service;

import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTourServiceTest {
    @Test
    void addAndRemove() {
        TourService svc = new InMemoryTourService();
        Tour t = new Tour("T1", "A", "B", 5.0, "/img.png");
        svc.addTour(t);
        List<Tour> all = svc.getAllTours();
        assertEquals(1, all.size());
        svc.removeTour(t);
        assertTrue(svc.getAllTours().isEmpty());
    }
}
