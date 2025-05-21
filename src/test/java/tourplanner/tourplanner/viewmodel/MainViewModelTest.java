package tourplanner.tourplanner.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainViewModelTest {
    @Test
    void initiallyEmpty() {
        assertTrue(new MainViewModel().getTours().isEmpty());
    }
}
