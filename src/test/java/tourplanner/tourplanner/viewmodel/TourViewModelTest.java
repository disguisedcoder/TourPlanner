package tourplanner.tourplanner.viewmodel;

import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;

import static org.junit.jupiter.api.Assertions.*;

class TourViewModelTest {

    @Test
    void properties_reflectModelAndBack() {
        Tour model = new Tour(
                "vm", "X", "Y", 10,
                30, "bike", "d", "/img");

        TourViewModel vm = new TourViewModel(model);

        assertEquals("vm", vm.nameProperty().get());

        vm.nameProperty().set("new");
        vm.distanceProperty().set(99);

        Tour copy = vm.toModel();

        assertAll(
                () -> assertEquals("new", copy.getName()),
                () -> assertEquals(99,    copy.getDistance(), 0.0001),
                () -> assertEquals("bike", copy.getTransportType())
        );
    }
}
