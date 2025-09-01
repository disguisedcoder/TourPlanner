package service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TransportType;
import tourplanner.tourplanner.persistence.TourLogRepository;
import tourplanner.tourplanner.persistence.TourRepository;
import tourplanner.tourplanner.service.ReportService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private TourRepository tourRepository;
    @Mock private TourLogRepository logRepository;

    @TempDir
    Path tempDir;

    private ReportService newService() {
        return new ReportService(tourRepository, logRepository);
    }

    @Test
    void generateTourReport_writesPdfWithTourDetails() throws Exception {
        // Arrange
        var service = newService();
        var tour = Tour.builder()
                .name("Wien – Graz")
                .fromLocation("Wien")
                .toLocation("Graz")
                .distance(213.7)
                .description("Feine Tour an der Mur")
                .estimatedTime(132)
                .transportType(TransportType.DRIVING_CAR)
                .build();

        Path target = tempDir.resolve("tour-report.pdf");

        // Act
        service.generateTourReport(tour, target);

        // Assert: Datei existiert und ist nicht trivial leer
        assertThat(Files.exists(target)).isTrue();
        assertThat(Files.size(target)).isGreaterThan(100L);

        // PDF-Text prüfen
        String text = extractText(target);

        // Überschriften
        assertThat(text).contains("Tour Report");
        assertThat(text).contains("Tour Details");

        // Name + Felder
        assertThat(text).contains("Tour: Wien – Graz");
        assertThat(text).contains("From").contains("Wien");
        assertThat(text).contains("To").contains("Graz");
        assertThat(text).contains("Distance [km]");
        assertThat(text).contains("Est. time [min]");
        assertThat(text).contains("Transport");

        // Werte (so wie im Code formatiert – String.valueOf(...))
        assertThat(text).contains("213.7");
        assertThat(text).contains("132");

        // Transport-Label aus dem Enum
        assertThat(text).contains(TransportType.DRIVING_CAR.getLabel());

        // aktuell ungenutzte Repos
        verifyNoInteractions(tourRepository, logRepository);
    }

    @Test
    void generateTourReport_throwsIOException_whenTargetIsDirectory() {
        var service = newService();
        var tour = Tour.builder()
                .name("Dir Test")
                .fromLocation("A")
                .toLocation("B")
                .distance(1.0)
                .estimatedTime(1)
                .transportType(TransportType.FOOT_WALKING)
                .build();

        // absichtlich ein Verzeichnis als "Ziel" übergeben
        Path targetIsDir = tempDir;

        assertThatThrownBy(() -> service.generateTourReport(tour, targetIsDir))
                .isInstanceOf(IOException.class);
    }

    @Test
    void generateTourReport_supportsUmlautsAndUnicode() throws Exception {
        var service = newService();
        var tour = Tour.builder()
                .name("Über den Kahlenberg – Spaziertour")
                .fromLocation("Mödling")
                .toLocation("Wörthersee")
                .distance(12.5)
                .estimatedTime(95)
                .transportType(TransportType.CYCLING_REGULAR)
                .build();

        Path target = tempDir.resolve("umlaut.pdf");
        service.generateTourReport(tour, target);

        String text = extractText(target);
        assertThat(text).contains("Über den Kahlenberg – Spaziertour");
        assertThat(text).contains("Mödling");
        assertThat(text).contains("Wörthersee");
        assertThat(text).contains(TransportType.CYCLING_REGULAR.getLabel());
    }

    // ------- helper -------

    private static String extractText(Path pdf) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (PdfDocument doc = new PdfDocument(new PdfReader(pdf.toString()))) {
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                sb.append(PdfTextExtractor.getTextFromPage(
                        doc.getPage(i), new SimpleTextExtractionStrategy()
                )).append('\n');
            }
        }
        return sb.toString();
    }
}
