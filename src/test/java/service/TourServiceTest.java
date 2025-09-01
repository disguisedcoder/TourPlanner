package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.persistence.TourRepository;
import tourplanner.tourplanner.service.OpenRouteServiceDirectionsService;
import tourplanner.tourplanner.service.OpenRouteServiceGeocodeSearchService;
import tourplanner.tourplanner.service.TourServiceImpl;
import tourplanner.tourplanner.service.response.Feature;
import tourplanner.tourplanner.service.response.GeocodeSearchResponse;
import tourplanner.tourplanner.service.response.GeometryResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für TourServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class TourServiceTest {

    @Mock private TourRepository tourRepository;

    // 1. Geocoder: wird in addTour(...) direkt verwendet
    @Mock private OpenRouteServiceGeocodeSearchService geocodeSearchService;

    // 2. Geocoder: wird in enrichWithGeoAndRoute(...) verwendet
    @Mock private OpenRouteServiceGeocodeSearchService geocode;

    @Mock private OpenRouteServiceDirectionsService directions;

    // KEIN @InjectMocks – wir instanziieren explizit, um die zwei gleichen Typen sauber zu verdrahten
    private TourServiceImpl tourService;

    @BeforeEach
    void setUp() {
        tourService = new TourServiceImpl(tourRepository, geocodeSearchService, geocode, directions);
    }

    // -------- Hilfsfunktion: echte Response-Objekte bauen (Koordinaten = [lon, lat]) --------
    private GeocodeSearchResponse geoResp(double lon, double lat) {
        GeometryResponse geometry = new GeometryResponse(List.of(lon, lat));
        Feature feature = new Feature(geometry);
        return new GeocodeSearchResponse(List.of(feature));
    }

    @Test
    void ensureGetAllToursWorksProperly() {
        // Given
        Tour tour = Tour.builder()
                .name("Test")
                .fromLocation("A")
                .toLocation("B")
                .build();

        when(tourRepository.findAll()).thenReturn(List.of(tour));

        // When
        var tours = tourService.getAllTours();

        // Then
        assertThat(tours).containsExactly(tour);
        verify(tourRepository, atLeastOnce()).findAll(); // Methode ruft aktuell findAll() zweimal
    }

    @Test
    void ensureAddTourSetsCoordinatesCallsDirectionsAndSaves() {
        // Arrange
        Tour tour = Tour.builder()
                .name("Wien ➜ Graz")
                .fromLocation("Wien")
                .toLocation("Graz")
                .build();

        // 1) addTour(): nutzt geocodeSearchService
        when(geocodeSearchService.getCoordinates("Wien"))
                .thenReturn(geoResp(16.3738, 48.2082)); // [lon, lat]
        when(geocodeSearchService.getCoordinates("Graz"))
                .thenReturn(geoResp(15.4395, 47.0707));

        // 2) enrichWithGeoAndRoute(): nutzt "geocode" (zweiter Service) und überschreibt die Felder erneut
        when(geocode.getCoordinates("Wien"))
                .thenReturn(geoResp(16.4, 48.22)); // absichtlich leicht anders, um Override zu prüfen
        when(geocode.getCoordinates("Graz"))
                .thenReturn(geoResp(15.45, 47.08));

        // 3) Directions-Ergebnis mocken
        OpenRouteServiceDirectionsService.DirectionsResult dir = mock(OpenRouteServiceDirectionsService.DirectionsResult.class);
        when(dir.distanceKm()).thenReturn(200.0);
        when(dir.durationMinutes()).thenReturn(120);
        when(dir.geometryGeoJson()).thenReturn("{\"type\":\"LineString\",\"coordinates\":[...]}");
        when(directions.route(any(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Optional.of(dir));

        // Act
        tourService.addTour(tour);

        // Assert: gespeicherte Tour prüfen
        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourRepository).save(captor.capture());
        Tour saved = captor.getValue();

        // Endwerte kommen aus dem ZWEITEN Geocoder; Mapping: [lon,lat] -> setLng(lon) & setLat(lat)
        assertThat(saved.getFromLat()).isEqualTo(48.22);
        assertThat(saved.getFromLng()).isEqualTo(16.4);
        assertThat(saved.getToLat()).isEqualTo(47.08);
        assertThat(saved.getToLng()).isEqualTo(15.45);

        // Directions übernommen
        assertThat(saved.getDistance()).isEqualTo(200.0);
        assertThat(saved.getEstimatedTime()).isEqualTo(120);
        assertThat(saved.getRouteGeoJson()).isEqualTo("{\"type\":\"LineString\",\"coordinates\":[...]}");

        // Interaktionen
        verify(geocodeSearchService).getCoordinates("Wien");
        verify(geocodeSearchService).getCoordinates("Graz");
        verify(geocode).getCoordinates("Wien");
        verify(geocode).getCoordinates("Graz");
        verify(directions).route(any(), eq(48.22), eq(16.4), eq(47.08), eq(15.45));
    }

    @Test
    void ensureAddTourStillSavesWhenDirectionsEmpty() {
        // Arrange
        Tour tour = Tour.builder()
                .name("Ohne Directions")
                .fromLocation("X")
                .toLocation("Y")
                .build();

        when(geocodeSearchService.getCoordinates("X")).thenReturn(geoResp(10.0, 50.0));
        when(geocodeSearchService.getCoordinates("Y")).thenReturn(geoResp(11.0, 51.0));
        when(geocode.getCoordinates("X")).thenReturn(geoResp(10.1, 50.1));
        when(geocode.getCoordinates("Y")).thenReturn(geoResp(11.1, 51.1));
        when(directions.route(any(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Optional.empty());

        // Act
        tourService.addTour(tour);

        // Assert
        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourRepository).save(captor.capture());
        Tour saved = captor.getValue();

        assertThat(saved.getFromLat()).isEqualTo(50.1);
        assertThat(saved.getFromLng()).isEqualTo(10.1);
        assertThat(saved.getToLat()).isEqualTo(51.1);
        assertThat(saved.getToLng()).isEqualTo(11.1);
        // GeoJSON bleibt unverändert (null), wenn Directions leer sind
        assertThat(saved.getRouteGeoJson()).isNull();
    }

    @Test
    void ensureUpdateTourRegeocodesAndSaves() {
        // Arrange
        Tour tour = Tour.builder()
                .name("Update mich")
                .fromLocation("A")
                .toLocation("B")
                .build();

        when(geocode.getCoordinates("A")).thenReturn(geoResp(14.0, 46.0));
        when(geocode.getCoordinates("B")).thenReturn(geoResp(15.0, 47.0));

        OpenRouteServiceDirectionsService.DirectionsResult dir = mock(OpenRouteServiceDirectionsService.DirectionsResult.class);
        when(dir.distanceKm()).thenReturn(99.9);
        when(dir.durationMinutes()).thenReturn(240);
        when(dir.geometryGeoJson()).thenReturn("{\"type\":\"LineString\"}");
        when(directions.route(any(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Optional.of(dir));

        // Act
        tourService.updateTour(tour);

        // Assert
        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourRepository).save(captor.capture());
        Tour saved = captor.getValue();

        assertThat(saved.getFromLat()).isEqualTo(46.0);
        assertThat(saved.getFromLng()).isEqualTo(14.0);
        assertThat(saved.getToLat()).isEqualTo(47.0);
        assertThat(saved.getToLng()).isEqualTo(15.0);
        assertThat(saved.getDistance()).isEqualTo(99.9);
        assertThat(saved.getEstimatedTime()).isEqualTo(240);
        assertThat(saved.getRouteGeoJson()).isEqualTo("{\"type\":\"LineString\"}");
    }

    @Test
    void ensureRemoveTourThrowsOnMissingId() {
        // IDs werden per JPA generiert; ein frisches Objekt hat id=0L -> Exception erwartet
        Tour tour = Tour.builder()
                .name("No ID")
                .fromLocation("X")
                .toLocation("Y")
                .build();

        assertThrows(IllegalArgumentException.class, () -> tourService.removeTour(tour));
        verifyNoInteractions(tourRepository);
    }

    @Test
    void ensureRemoveTourDeletesById() {
        // Für den Happy Path mocken wir eine Tour mit gesetzter ID
        Tour tourWithId = mock(Tour.class);
        when(tourWithId.getId()).thenReturn(7L);

        tourService.removeTour(tourWithId);

        verify(tourRepository).deleteById(7L);
    }
}
