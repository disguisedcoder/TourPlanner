package tourplanner.tourplanner;

import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import static org.junit.jupiter.api.Assertions.*;

class TourViewModelRoundTripTest {

    @Test
    void toModel_reflectsEditedProperties() {
        Tour base = new Tour("Base", "X", "Y", 1.0,
                0, "", "", "/img");
        TourViewModel vm = new TourViewModel(base);

        vm.nameProperty().set("Changed");
        vm.distanceProperty().set(9.5);

        Tour copy = vm.toModel();

        assertEquals("Changed", copy.getName());
        assertEquals(9.5,       copy.getDistance(), 0.0001);
    }
}
