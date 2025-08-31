package tourplanner.tourplanner.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.model.TransportType;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class EditController {

    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextField estimateField;
    @FXML private TextArea  descriptionArea;
    @FXML private ComboBox<TransportType> transportCombo;

    private final MainViewModel vm;
    private TourViewModel original; // referenz auf die zu bearbeitende Tour

    public void showDialog(TourViewModel tour) {
        this.original = tour;

        FXMLLoader f = new FXMLLoader(getClass().getResource("/tourplanner/tourplanner/view/EditTour.fxml"));
        f.setController(this);

        Region content;
        try { content = f.load(); } catch (IOException e) { throw new RuntimeException(e); }

        // ComboBox befüllen
        transportCombo.setItems(FXCollections.observableArrayList(TransportType.values()));

        // Felder vorbefüllen aus dem ViewModel
        nameField.setText(original.getNameProperty().get());
        fromField.setText(original.getFromProperty().get());
        toField.setText(original.getToProperty().get());
        distField.setText(original.getDistanceProperty().get());
        estimateField.setText(original.getEstimatedTimeProperty().get());
        descriptionArea.setText(original.getDescriptionProperty().get());
        transportCombo.setValue(original.getTransportTypeProperty().get()); // Enum

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Tour");
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // einfache Validierung vor dem Schließen (wie bei Create)
        final Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            String name = trim(nameField.getText());
            String from = trim(fromField.getText());
            String to   = trim(toField.getText());
            if (name.isEmpty()) {
                showError("Name darf nicht leer sein."); nameField.requestFocus(); ev.consume(); return;
            }
            if (!isAllowedLocation(from)) {
                showHelp("From ist ungültig.", from); fromField.requestFocus(); ev.consume(); return;
            }
            if (!isAllowedLocation(to)) {
                showHelp("To ist ungültig.", to); toField.requestFocus(); ev.consume(); return;
            }
        });

        Optional<ButtonType> res = dlg.showAndWait();
        if (res.orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        // numerische Checks (wie bei dir gewohnt)
        try { Double.parseDouble(distField.getText()); }
        catch (Exception e) { new Alert(Alert.AlertType.ERROR,"Distance must be a number").showAndWait(); return; }
        try { Integer.parseInt(estimateField.getText()); }
        catch (Exception e) { new Alert(Alert.AlertType.ERROR,"Estimated time must be an integer").showAndWait(); return; }

        // Werte zurück ins (bereits in der ListView angezeigte) ViewModel schreiben
        original.getNameProperty().set(trim(nameField.getText()));
        original.getFromProperty().set(trim(fromField.getText()));
        original.getToProperty().set(trim(toField.getText()));
        original.getDistanceProperty().set(trim(distField.getText()));
        original.getEstimatedTimeProperty().set(trim(estimateField.getText()));
        original.getDescriptionProperty().set(descriptionArea.getText());
        original.getTransportTypeProperty().set(transportCombo.getValue());

        // Persistieren
        vm.updateTour(original); // -> siehe Schritt 3

        // Optional: Erfolgsmeldung
        // new Alert(Alert.AlertType.INFORMATION, "Tour aktualisiert.").showAndWait();
    }

    private static String trim(String s){ return s==null ? "" : s.trim(); }

    // einfache Heuristik (wie zuvor): Ort/PLZ/Land/Adresse akzeptieren
    private static boolean isAllowedLocation(String s) {
        if (s == null) return false;
        String t = s.trim();
        if (t.isEmpty()) return false;
        if (t.matches("\\d{4,5}")) return true; // PLZ
        if (t.contains(",") && (t.matches(".*\\d+.*") || t.matches(".*\\b\\d{4,5}\\b.*"))) return true; // Adresse
        return t.matches("[\\p{L} .'-]{2,}"); // Ort/Land
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
    private void showHelp(String header, String value) {
        String body = """
            Erlaubte Eingaben:
              • Ort (z. B. "Wien")
              • PLZ (z. B. "1020")
              • Land (z. B. "Österreich")
              • Vollständige Adresse (z. B. "Lassallestraße 1, 1020 Wien, Österreich")
            """;
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Ungültige Eingabe");
        a.setHeaderText(header);
        TextArea ta = new TextArea(body + (value!=null ? "\n\nEingegeben: "+value : ""));
        ta.setEditable(false); ta.setWrapText(true);
        a.getDialogPane().setExpandableContent(ta);
        a.getDialogPane().setExpanded(true);
        a.showAndWait();
    }
}
