//package service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import tourplanner.tourplanner.model.Tour;
//import tourplanner.tourplanner.model.TransportType;
//import tourplanner.tourplanner.persistence.TourRepository;
//import tourplanner.tourplanner.service.OpenRouteServiceDirectionsService;
//import tourplanner.tourplanner.service.OpenRouteServiceGeocodeSearchService;
//import tourplanner.tourplanner.service.TourService;
//import tourplanner.tourplanner.service.TourServiceImpl;
//
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TourServiceTest {
//    private TourService tourService;
//    private OpenRouteServiceGeocodeSearchService geocodeSearchService;
//    private OpenRouteServiceGeocodeSearchService geocode;
//    private OpenRouteServiceDirectionsService directions;
//
//    @Mock
//    private TourRepository tourRepository;
//
//    @BeforeEach
//    void setUp() {
//        tourService = new TourServiceImpl(tourRepository,geocodeSearchService,geocode,directions);
//    }
//
//    @Test
//    void ensureGetAllToursWorksProperly() {
//        // Given
//        Tour tour = Tour.builder()
//                .name("Test")
//                .fromLocation("abc")
//                .toLocation("abc")
//                .distance(123)
//                .description("abc")
//                .estimatedTime(123)
//                .transportType(TransportType.valueOf("abc"))
//                .build();
//        when(tourRepository.findAll()).thenReturn(java.util.List.of(tour));
//
//        // When
//        List<Tour> tours = tourService.getAllTours();
//
//        // Then
//        assertThat(tours).contains(tour);
//    }
//
//    @Test
//    void ensureAddTourSetsCoordinatesAndSavesTour() {
//        // Arrange
//        Tour tour = Tour.builder()
//                .name("Test")
//                .fromLocation("Wien")
//                .toLocation("Graz")
//                .build();
//
//        // Geocode-Response simulieren
//        var geometryFrom = new tourplanner.tourplanner.service.response.GeometryResponse(List.of(48.2082, 16.3738));
//        var featureFrom = new tourplanner.tourplanner.service.response.GeocodeFeatureResponse(geometryFrom);
//        var responseFrom = new tourplanner.tourplanner.service.response.GeocodeSearchResponse(List.of(featureFrom));
//
//        var geometryTo = new tourplanner.tourplanner.service.response.GeometryResponse(List.of(47.0707, 15.4395));
//        var featureTo = new tourplanner.tourplanner.service.response.GeocodeFeatureResponse(geometryTo);
//        var responseTo = new tourplanner.tourplanner.service.response.GeocodeSearchResponse(List.of(featureTo));
//
//        geocodeSearchService = mock(OpenRouteServiceGeocodeSearchService.class);
//        tourService = new TourServiceImpl(tourRepository, geocodeSearchService);
//
//        when(geocodeSearchService.getCoordinates("Wien")).thenReturn(responseFrom);
//        when(geocodeSearchService.getCoordinates("Graz")).thenReturn(responseTo);
//
//        // Act
//        tourService.addTour(tour);
//
//        // Assert
//        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
//        verify(tourRepository).save(captor.capture());
//        Tour savedTour = captor.getValue();
//
//        assertThat(savedTour.getFromLat()).isEqualTo(48.2082);
//        assertThat(savedTour.getFromLng()).isEqualTo(16.3738);
//        assertThat(savedTour.getToLat()).isEqualTo(47.0707);
//        assertThat(savedTour.getToLng()).isEqualTo(15.4395);
//    }
//
//
//}
