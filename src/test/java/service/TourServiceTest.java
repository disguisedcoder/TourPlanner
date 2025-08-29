package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.persistence.TourRepository;
import tourplanner.tourplanner.service.TourService;
import tourplanner.tourplanner.service.TourServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {
    private TourService tourService;

    @Mock
    private TourRepository tourRepository;

    @BeforeEach
    void setUp() {
        tourService = new TourServiceImpl(tourRepository);
    }

    @Test
    void ensureGetAllToursWorksProperly() {
        // Given
        Tour tour = Tour.builder()
                .name("Test")
                .fromLocation("abc")
                .toLocation("abc")
                .distance(123)
                .description("abc")
                .estimatedTime(123)
                .transportType("abc")
                .build();
        when(tourRepository.findAll()).thenReturn(java.util.List.of(tour));

        // When
        List<Tour> tours = tourService.getAllTours();

        // Then
        assertThat(tours).contains(tour);
    }
}
