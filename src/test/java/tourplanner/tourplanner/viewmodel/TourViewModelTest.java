package tourplanner.tourplanner.viewmodel;

import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TourViewModelTest {
    @Test
    void validTour() {
        assertTrue(new TourViewModel(new Tour("X", "A", "B", 1, "")).isValid());
    }

    @Test
    void invalidName() {
        assertFalse(new TourViewModel(new Tour("", "A", "B", 1, "")).isValid());
    }
}
