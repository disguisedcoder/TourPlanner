package tourplanner.tourplanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.InMemoryTourLogService;
import tourplanner.tourplanner.service.InMemoryTourService;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import static org.junit.jupiter.api.Assertions.*;

class MainViewModelCrudTest {

    private MainViewModel vm;

    @BeforeEach
    void setUp() {
        MainViewModel.init(new InMemoryTourService(), new InMemoryTourLogService());
        vm = MainViewModel.getInstance();
        vm.refreshTours();
        vm.tours.clear();
    }

    @Test
    void addAndDeleteTour_updatesObservableList() {
        TourViewModel tvm = new TourViewModel(
                new Tour("AddDel", "A", "B", 2.0, "/view/images/demo.png"));

        vm.addTour(tvm);
        assertEquals(1, vm.tours.size());

        vm.selectedTourProperty().set(tvm);
        vm.deleteSelectedTour();
        assertTrue(vm.tours.isEmpty());
    }
}
