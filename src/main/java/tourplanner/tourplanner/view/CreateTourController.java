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
public class CreateTourController {
    @FXML
    private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextField estimateField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField imgField;


    @FXML private ComboBox<TransportType> transportField;

    private final MainViewModel vm;

    public void showDialog() {
        FXMLLoader f = new FXMLLoader(getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml"));

        f.setController(this);
        Region content = null;
        try {
            content = f.load();
            distField.setEditable(false);
            estimateField.setEditable(false);
            distField.setPromptText("auto");
            estimateField.setPromptText("auto");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        transportField.setItems(FXCollections.observableArrayList(TransportType.values()));
        if (transportField.getValue() == null) {
            transportField.setValue(TransportType.DRIVING_CAR);
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dlg.setTitle("Create Tour");

        final Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            if (!validateNameAndAddresses()) {
                ev.consume();
            }
        });

        Optional<ButtonType> res = dlg.showAndWait();
        if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
//            try {
//                Double.parseDouble(distField.getText());
//            }
//            catch (Exception e) { new Alert(Alert.AlertType.ERROR,
//                    "Distance must be a number").showAndWait(); return; }
//            try {
//                Integer.parseInt(estimateField.getText());
//            }
//            catch (Exception e) {
//                new Alert(Alert.AlertType.ERROR, "Estimated time must be an integer").showAndWait();
//                return;
//            }

            TourViewModel model = new TourViewModel(
                    nameField.textProperty(),
                    fromField.textProperty(),
                    toField.textProperty(),
                    distField.textProperty(),
                    descriptionArea.textProperty(),
                    transportField.valueProperty(),
                    estimateField.textProperty()
            );

            vm.addTour(model);
        }
    }

    private boolean validateNameAndAddresses() {
        final String name = trimOrEmpty(nameField.getText());
        final String from = trimOrEmpty(fromField.getText());
        final String to   = trimOrEmpty(toField.getText());

        // Name prüfen
        if (name.isBlank()) {
            showFormatHelp("Name", "Darf nicht leer sein (z. B. \"Donau-Radtour\").", null);
            nameField.requestFocus();
            return false;
        }

        if (!isAllowedLocation(from)) {
            showFormatHelp("From", ADDRESS_HELP_TEXT, "Eingegeben: " + from);
            fromField.requestFocus();
            return false;
        }
        if (!isAllowedLocation(to)) {
            showFormatHelp("To", ADDRESS_HELP_TEXT, "Eingegeben: " + to);
            toField.requestFocus();
            return false;
        }
        return true;
    }

    private static String trimOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }
    private static final String ADDRESS_HELP_TEXT = """
  Erlaubte Eingaben::
  • Ort (z. B. "Wien")
  • PLZ (z. B. "1020")
  • Land (z. B. "Österreich")
  • Vollständige Adresse (z. B. "Lassallestraße 1, 1020 Wien, Österreich")
  """;

    private static void showFormatHelp(String fieldLabel, String helpBody, String extraLine) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Ungültige Eingabe");
        a.setHeaderText("Bitte gültiges Format für \"" + fieldLabel + "\" verwenden");
        StringBuilder msg = new StringBuilder(helpBody == null ? "" : helpBody);
        if (extraLine != null && !extraLine.isBlank()) {
            msg.append("\n\n").append(extraLine);
        }
        TextArea ta = new TextArea(msg.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        a.getDialogPane().setExpandableContent(ta);
        a.getDialogPane().setExpanded(true);
        a.showAndWait();
    }
    private static boolean isAllowedLocation(String s) {
        if (s == null) return false;
        String t = s.trim();
        if (t.isEmpty()) return false;

        // PLZ: 4–5 Ziffern
        if (t.matches("\\d{4,5}")) return true;

        // Vollständige Adresse: enthält Komma UND (irgendeine Zahl ODER eine 4/5-stellige PLZ irgendwo)
        if (t.contains(",") && (t.matches(".*\\d+.*") || t.matches(".*\\b\\d{4,5}\\b.*"))) return true;

        // Ort/Land: nur Buchstaben (inkl. Umlaute), Leerzeichen, Punkt, Apostroph, Bindestrich; mind. 2 Zeichen
        if (t.matches("[\\p{L} .'-]{2,}")) return true;

        return false;
    }
}
