package tourplanner.tourplanner.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TourLogModelTest {
    @Test
    void emptyLogsInit() {
        assertTrue(new TourLogViewModel("No").getLogs().isEmpty());
    }
}
