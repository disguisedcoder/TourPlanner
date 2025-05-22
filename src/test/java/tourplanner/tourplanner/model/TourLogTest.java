package tourplanner.tourplanner.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TourLogTest {

    @Test
    void constructor_setsValues() {
        LocalDateTime now = LocalDateTime.now();

        TourLog log = new TourLog(
                "Tour-1", now, "ok",
                3, 15.0, 120, "4");

        assertAll(
                () -> assertEquals("Tour-1",  log.tourNameProperty().get()),
                () -> assertEquals(now,       log.dateTimeProperty().get()),
                () -> assertEquals("ok",      log.commentProperty().get()),
                () -> assertEquals(3,         log.difficultyProperty().get()),
                () -> assertEquals(15.0,      log.totalDistanceProperty().get(), 0.0001),
                () -> assertEquals(120,       log.totalTimeProperty().get()),
                () -> assertEquals("4",       log.ratingProperty().get())
        );
    }
}
