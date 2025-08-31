package tourplanner.tourplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TourLog;
import tourplanner.tourplanner.persistence.TourLogRepository;
import tourplanner.tourplanner.persistence.TourRepository;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final TourRepository tourRepo;
    private final TourLogRepository logRepo;
    //private final MapSnapshotService mapSnap;
    //private final TourMetricsCalculator metrics = new TourMetricsCalculator();

    public void generateTourReport(Tour tour, Path target) throws IOException {
        //mapSnap.ensureMapImage(tour);
        //List<TourLog> logs = logRepo.findByTourOrderByDate(tour);

        try(PdfWriter writer = new PdfWriter(target.toString());
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf)) {

            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

            // Title
            doc.add(new Paragraph(new Text("Tour Report").setFont(bold).setFontSize(18)));
            doc.add(new Paragraph(new Text("Tour: " + tour.getName()).setFont(italic).setFontSize(12)).setMarginBottom(10));

            addMetaTable(tour, doc, bold);
            //addMapImage(tour, doc, bold);
            //addLogsTable(logs, doc, bold);
        }
    }

    private void addMetaTable(Tour tour, Document doc, PdfFont boldFont) {
        doc.add(new Paragraph("\nTour Details").setFont(boldFont).setFontSize(14).setMarginTop(10));

        Table table = new Table(UnitValue.createPercentArray(2))
                .useAllAvailableWidth();

        table.addCell(new Cell().add(new Paragraph("From").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(tour.getFromLocation())));

        table.addCell(new Cell().add(new Paragraph("To").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(tour.getToLocation())));

        table.addCell(new Cell().add(new Paragraph("Distance [km]").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(tour.getDistance()))));

        table.addCell(new Cell().add(new Paragraph("Est. time [min]").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(tour.getEstimatedTime()))));

        table.addCell(new Cell().add(new Paragraph("Transport").setFont(boldFont)));
        table.addCell(new Cell().add(new Paragraph(tour.getTransportType())));

//        int popularity = metrics.calculatePopularity(logRepo.findAll(), tour);
//        table.addCell(new Cell().add(new Paragraph("Popularity").setFont(boldFont)));
//        table.addCell(new Cell().add(new Paragraph(popularity + " Stars")));
//
//        int child = metrics.calculateChildFriendliness(logRepo.findAll(), tour);
//        table.addCell(new Cell().add(new Paragraph("Child-friendly").setFont(boldFont)));
//        table.addCell(new Cell().add(new Paragraph(child + " Stars")));

        doc.add(table);
    }

}
