package viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TransportType;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import static org.assertj.core.api.Assertions.assertThat;

class TourViewModelTest {

    @Test
    void ctor_fromTour_setsAllProperties() {
        // Given
        Tour tour = Tour.builder()
                .name("Wien–Graz")
                .fromLocation("Wien")
                .toLocation("Graz")
                .distance(200.5)
                .description("Feine Tour")
                .estimatedTime(123)
                .transportType(TransportType.DRIVING_CAR)
                .build();
        tour.setId(42L);
        tour.setFromLat(48.2082);
        tour.setFromLng(16.3738);
        tour.setToLat(47.0707);
        tour.setToLng(15.4395);
        tour.setRouteGeoJson("{\"type\":\"LineString\"}");
        // When
        TourViewModel vm = new TourViewModel(tour);

        // Then
        assertThat(vm.getId()).isEqualTo(42L);
        assertThat(vm.getNameProperty().get()).isEqualTo("Wien–Graz");
        assertThat(vm.getFromProperty().get()).isEqualTo("Wien");
        assertThat(vm.getToProperty().get()).isEqualTo("Graz");
        assertThat(vm.getDistanceProperty().get()).isEqualTo("200.5");
        assertThat(vm.getDescriptionProperty().get()).isEqualTo("Feine Tour");
        assertThat(vm.getEstimatedTimeProperty().get()).isEqualTo("123");
        assertThat(vm.getTransportTypeProperty().get()).isEqualTo(TransportType.DRIVING_CAR);
        assertThat(vm.getFromLatProperty().get()).isEqualTo("48.2082");
        assertThat(vm.getFromLngProperty().get()).isEqualTo("16.3738");
        assertThat(vm.getToLatProperty().get()).isEqualTo("47.0707");
        assertThat(vm.getToLngProperty().get()).isEqualTo("15.4395");
        assertThat(vm.getRouteGeoJsonProperty().get()).isEqualTo("{\"type\":\"LineString\"}");
    }

    @Test
    void toModel_roundtrip_preservesFields_andId() {
        // Given
        Tour tour = Tour.builder()
                .name("X").fromLocation("A").toLocation("B")
                .distance(1.2).description("desc").estimatedTime(5)
                .transportType(TransportType.FOOT_WALKING)

                .build();
        tour.setId(7L);
        tour.setFromLat(1.0);
        tour.setFromLng(2.0);
        tour.setToLat(3.0);
        tour.setToLng(4.0);
        tour.setRouteGeoJson("geo");


        TourViewModel vm = new TourViewModel(tour);

        // When
        Tour model = vm.toModel();

        // Then
        assertThat(model.getId()).isEqualTo(7L);
        assertThat(model.getName()).isEqualTo("X");
        assertThat(model.getFromLocation()).isEqualTo("A");
        assertThat(model.getToLocation()).isEqualTo("B");
        assertThat(model.getDistance()).isEqualTo(1.2);
        assertThat(model.getDescription()).isEqualTo("desc");
        assertThat(model.getEstimatedTime()).isEqualTo(5);
        assertThat(model.getTransportType()).isEqualTo(TransportType.FOOT_WALKING);
        assertThat(model.getFromLat()).isEqualTo(1.0);
        assertThat(model.getFromLng()).isEqualTo(2.0);
        assertThat(model.getToLat()).isEqualTo(3.0);
        assertThat(model.getToLng()).isEqualTo(4.0);
        assertThat(model.getRouteGeoJson()).isEqualTo("geo");
    }

    @Test
    void toModel_parsesInvalidNumbers_asZero_and_ignoresNullLatLngProps() {
        // Given: ctor mit gebundenem Transport-Type (erste ctor-Variante)
        StringProperty name = new SimpleStringProperty("BadNums");
        StringProperty from = new SimpleStringProperty("From");
        StringProperty to   = new SimpleStringProperty("To");
        StringProperty distance = new SimpleStringProperty("abc");  // invalid -> 0.0
        StringProperty desc = new SimpleStringProperty("d");
        ObjectProperty<TransportType> extTransport = new SimpleObjectProperty<>(TransportType.CYCLING_REGULAR);
        StringProperty est = new SimpleStringProperty("oops");      // invalid -> 0

        TourViewModel vm = new TourViewModel(name, from, to, distance, desc, extTransport, est);
        // lat/lng Properties bleiben NULL in dieser ctor-Variante

        // When
        Tour model = vm.toModel();

        // Then
        assertThat(model.getDistance()).isEqualTo(0.0);
        assertThat(model.getEstimatedTime()).isEqualTo(0);
        // lat/lng bleiben bei default 0.0, weil Properties null waren
        assertThat(model.getFromLat()).isEqualTo(0.0);
        assertThat(model.getFromLng()).isEqualTo(0.0);
        assertThat(model.getToLat()).isEqualTo(0.0);
        assertThat(model.getToLng()).isEqualTo(0.0);
        assertThat(model.getTransportType()).isEqualTo(TransportType.CYCLING_REGULAR);
    }

    @Test
    void bindingCtor_bindsTransportType_fromExternalProperty() {
        // Given
        StringProperty name = new SimpleStringProperty("n");
        StringProperty from = new SimpleStringProperty("a");
        StringProperty to   = new SimpleStringProperty("b");
        StringProperty distance = new SimpleStringProperty("1.0");
        StringProperty desc = new SimpleStringProperty("-");
        ObjectProperty<TransportType> ext = new SimpleObjectProperty<>(TransportType.FOOT_WALKING);
        StringProperty est = new SimpleStringProperty("10");

        TourViewModel vm = new TourViewModel(name, from, to, distance, desc, ext, est);

        // When: externen Wert ändern -> vm-Property folgt wegen bind()
        ext.set(TransportType.DRIVING_CAR);

        // Then
        assertThat(vm.getTransportTypeProperty().get()).isEqualTo(TransportType.DRIVING_CAR);

        // And toModel übernimmt den gebundenen Wert
        Tour model = vm.toModel();
        assertThat(model.getTransportType()).isEqualTo(TransportType.DRIVING_CAR);
    }
}
