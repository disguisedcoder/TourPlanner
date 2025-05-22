package tourplanner.tourplanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.InMemoryTourLogService;
import tourplanner.tourplanner.service.InMemoryTourService;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import static org.junit.jupiter.api.Assertions.*;

class MainViewModelFilterTest {

    private MainViewModel vm;

    @BeforeEach
    void setUp() {
        MainViewModel.init(new InMemoryTourService(), new InMemoryTourLogService());
        vm = MainViewModel.getInstance();
        vm.refreshTours();
        vm.tours.clear();

        vm.addTour(new TourViewModel(
                new Tour("Alpenfahrt", "x", "y", 1.0, "/view/images/demo.png")));
        vm.addTour(new TourViewModel(
                new Tour("CityTrip",   "x", "y", 1.0, "/view/images/demo.png")));
    }

    @Test
    void findTours_filtersIgnoringCase() {
        vm.findTours("alp");
        assertEquals(1, vm.tours.size());
        assertEquals("Alpenfahrt", vm.tours.get(0).nameProperty().get());

        vm.findTours("");
        assertEquals(2, vm.tours.size());
    }
}
