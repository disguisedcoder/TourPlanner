package viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TransportType;
import tourplanner.tourplanner.service.ReportService;
import tourplanner.tourplanner.service.TourLogService;
import tourplanner.tourplanner.service.TourService;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainViewModelTest {

    @Mock private TourService tourService;
    @Mock private TourLogService logService;
    @Mock private ReportService reportService;

    private MainViewModel vm;

    @BeforeEach
    void setUp() {
        vm = new MainViewModel(tourService, logService, reportService);
    }

    @Test
    void loadTours_populatesAllTours_fromService() {
        // Given
        Tour t1 = Tour.builder().name("T1").fromLocation("A").toLocation("B").distance(10).description("d").estimatedTime(5).transportType(TransportType.FOOT_WALKING).build();
        t1.setId(1L);
        Tour t2 = Tour.builder().name("T2").fromLocation("C").toLocation("D").distance(20).description("e").estimatedTime(15).transportType(TransportType.DRIVING_CAR).build();
        t2.setId(2L);
        when(tourService.getAllTours()).thenReturn(List.of(t1, t2));

        // When
        vm.loadTours();

        // Then
        assertThat(vm.allTours).hasSize(2);
        assertThat(vm.allTours.get(0).getNameProperty().get()).isEqualTo("T1");
        assertThat(vm.allTours.get(1).getNameProperty().get()).isEqualTo("T2");
        verify(tourService).getAllTours();
    }

    @Test
    void addTour_callsService_add_andReloadsFromService() {
        // KEIN .asString() – wir brauchen echte StringPropertys:
        TourViewModel tvmToAdd = new TourViewModel(
                new SimpleStringProperty("N"),
                new SimpleStringProperty("From"),
                new SimpleStringProperty("To"),
                new SimpleStringProperty("12.0"),
                new SimpleStringProperty("desc"),
                new SimpleObjectProperty<>(TransportType.CYCLING_REGULAR),
                new SimpleStringProperty("34")
        );

        // Nach addTour() lädt VM neu – definieren, was dann zurückkommt
        Tour returned = Tour.builder().name("Loaded").fromLocation("X").toLocation("Y")
                .distance(1.0).description("d").estimatedTime(1)
                .transportType(TransportType.FOOT_WALKING).build();
        returned.setId(99L);
        when(tourService.getAllTours()).thenReturn(List.of(returned));

        vm.addTour(tvmToAdd);

        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourService).addTour(captor.capture());
        Tour sent = captor.getValue();
        assertThat(sent.getName()).isEqualTo("N");
        assertThat(sent.getFromLocation()).isEqualTo("From");
        assertThat(sent.getToLocation()).isEqualTo("To");
        assertThat(sent.getDistance()).isEqualTo(12.0);
        assertThat(sent.getEstimatedTime()).isEqualTo(34);
        assertThat(sent.getTransportType()).isEqualTo(TransportType.CYCLING_REGULAR);

        assertThat(vm.allTours).hasSize(1);
        assertThat(vm.allTours.get(0).getNameProperty().get()).isEqualTo("Loaded");
        verify(tourService).getAllTours();
    }


    @Test
    void reportSelectedTour_success_callsReportService(@TempDir Path tmp) throws Exception {
        // Given: eine ausgewählte Tour setzen
        Tour base = Tour.builder().name("R1").fromLocation("A").toLocation("B").distance(2.0).description("d").estimatedTime(3).transportType(TransportType.DRIVING_CAR).build();
        base.setId(6L);
        TourViewModel selected = new TourViewModel(base);
        vm.selectedTourProperty().set(selected);

        Path target = tmp.resolve("report.pdf");

        // When
        vm.reportSelectedTour(target);

        // Then
        verify(reportService).generateTourReport(any(Tour.class), eq(target));
    }

    @Test
    void reportSelectedTour_throws_whenNoSelection() {
        // Given: keine Auswahl

        // Then
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> vm.reportSelectedTour(Path.of("irrelevant.pdf")));
        assertThat(ex).hasMessageContaining("Bitte zuerst");
        verifyNoInteractions(reportService);
    }

    @Test
    void reportSelectedTour_wrapsIOException_inUnchecked() throws Exception {
        // Given
        Tour base = Tour.builder().name("R2").fromLocation("A").toLocation("B").distance(2.0).description("d").estimatedTime(3).transportType(TransportType.FOOT_WALKING).build();
        base.setId(7L);
        vm.selectedTourProperty().set(new TourViewModel(base));

        doThrow(new IOException("boom")).when(reportService).generateTourReport(any(Tour.class), any(Path.class));

        // Then
        assertThatThrownBy(() -> vm.reportSelectedTour(Path.of("x.pdf")))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining("Report konnte nicht erstellt werden");
    }

    @Test
    void deleteSelectedTour_callsRemoveById_andReloadsList() {
        // Given: ausgewähltes VM mit ID
        Tour t = Tour.builder().name("ToDelete").fromLocation("A").toLocation("B").distance(1.0).description("d").estimatedTime(1).transportType(TransportType.FOOT_WALKING).build();
        t.setId(77L);
        TourViewModel selected = new TourViewModel(t);
        vm.selectedTourProperty().set(selected);

        // Nach dem Löschen lädt VM die Liste neu; hier: leer
        when(tourService.getAllTours()).thenReturn(List.of());

        // When
        vm.deleteSelectedTour();

        // Then: removeTour mit passender ID aufgerufen?
        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourService).removeTour(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(77L);

        // Liste neu geladen (leer)
        assertThat(vm.allTours).isEmpty();
        verify(tourService).getAllTours();
    }

    @Test
    void updateTour_callsService_andReloads() {
        // Given
        Tour existing = Tour.builder().name("Before").fromLocation("A").toLocation("B").distance(1.0).description("d").estimatedTime(1).transportType(TransportType.FOOT_WALKING).build();
        existing.setId(88L);
        TourViewModel vmToUpdate = new TourViewModel(existing);
        vmToUpdate.getNameProperty().set("After");
        vmToUpdate.getDistanceProperty().set("12.3");
        // Nach update -> loadTours()
        when(tourService.getAllTours()).thenReturn(List.of(existing));

        // When
        vm.updateTour(vmToUpdate);

        // Then
        ArgumentCaptor<Tour> captor = ArgumentCaptor.forClass(Tour.class);
        verify(tourService).updateTour(captor.capture());
        Tour sent = captor.getValue();
        assertThat(sent.getId()).isEqualTo(88L);
        assertThat(sent.getName()).isEqualTo("After");
        assertThat(sent.getDistance()).isEqualTo(12.3);

        verify(tourService).getAllTours();
        assertThat(vm.allTours).hasSize(1);
    }
}
