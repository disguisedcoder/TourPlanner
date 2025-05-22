package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;
import tourplanner.tourplanner.model.Tour;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class EditTourController {
    @FXML private TextField nameField, fromField, toField, distField, imgField;

    private final MainViewModel vm     = MainViewModel.getInstance();
    private       TourViewModel selectedTvm;

    // Default‐Konstruktor
    public EditTourController() {}

    /** Muss VOR showDialog() aufgerufen werden, um die Tour zu setzen. */
    public void setTour(TourViewModel tvm) {
        this.selectedTvm = tvm;
    }

    public void showDialog() {
        if (selectedTvm == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Kein Eintrag ausgewählt", ButtonType.OK)
                    .showAndWait();
            return;
        }

        try {
            // 1) FXML laden
            URL url = getClass().getResource(
                    "/tourplanner/tourplanner/view/CreateTour.fxml"
            );
            if (url == null) throw new RuntimeException("FXML nicht gefunden!");
            FXMLLoader f = new FXMLLoader(url);
            f.setController(this);
            Region content = f.load();

            // 2) Felder vorbelegen
            nameField.setText(selectedTvm.nameProperty().get());
            fromField.setText(selectedTvm.fromProperty().get());
            toField.setText(selectedTvm.toProperty().get());
            distField.setText(
                    String.valueOf(selectedTvm.distanceProperty().get())
            );
            imgField.setText(selectedTvm.imagePathProperty().get());

            // 3) Dialog aufbauen
            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.setTitle("Edit Tour");
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes()
                    .addAll(ButtonType.OK, ButtonType.CANCEL);

            // 4) anzeigen und warten
            Optional<ButtonType> res = dlg.showAndWait();
            if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // 5) neue Werte validieren & übernehmen
                double d;
                try {
                    d = Double.parseDouble(distField.getText());
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR,
                            "Distance must be a number")
                            .showAndWait();
                    return;
                }
                selectedTvm.nameProperty().set(nameField.getText());
                selectedTvm.fromProperty().set(fromField.getText());
                selectedTvm.toProperty().set(toField.getText());
                selectedTvm.distanceProperty().set(d);
                selectedTvm.imagePathProperty().set(imgField.getText());
            }

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden des Edit-Tour-Dialogs", e);
        }
    }
}
