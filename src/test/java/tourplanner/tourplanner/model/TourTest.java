package tourplanner.tourplanner.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    @Test
    void fullConstructor_setsAllFields() {
        Tour t = new Tour(
                "Test-Tour", "A", "B", 42.0,
                90, "car", "desc", "/view/images/demo.png");

        assertAll(
                () -> assertEquals("Test-Tour", t.getName()),
                () -> assertEquals("A",          t.getFrom()),
                () -> assertEquals("B",          t.getTo()),
                () -> assertEquals(42.0,         t.getDistance(), 0.0001),
                () -> assertEquals(90,           t.getEstimatedTime()),
                () -> assertEquals("car",        t.getTransportType()),
                () -> assertEquals("desc",       t.getDescription()),
                () -> assertEquals("/view/images/demo.png", t.getImagePath())
        );
    }
}