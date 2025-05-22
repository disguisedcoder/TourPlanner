package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.IOException;
import java.util.Optional;

public class EditTourController {

    /* ---------- FXML-Felder ---------- */
    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextField estimateField;
    @FXML private TextField transportField;
    @FXML private TextArea  descriptionArea;
    @FXML private TextField imgField;

    /* ---------- View-Model-Zugriff ---------- */
    private final MainViewModel vm = MainViewModel.getInstance();

    /* ---------- wird extern (z. B. aus TourListController) gesetzt ---------- */
    private TourViewModel tvm;
    public void setTour(TourViewModel tvm) { this.tvm = tvm; }

    /* ---------- Dialog anzeigen ---------- */
    public void showDialog() {
        TourViewModel tvm = this.tvm != null ? this.tvm : vm.selectedTourProperty().get();
        if (tvm == null) return;

        try {
            FXMLLoader f = new FXMLLoader(
                    getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml"));
            f.setController(this);
            Region content = f.load();

            /* Felder mit aktuellen Daten füllen */
            nameField.setText(tvm.nameProperty().get());
            fromField.setText(tvm.fromProperty().get());
            toField.setText(tvm.toProperty().get());
            distField.setText(String.valueOf(tvm.distanceProperty().get()));
            estimateField.setText(String.valueOf(tvm.estimatedTimeProperty().get()));
            transportField.setText(tvm.transportTypeProperty().get());
            descriptionArea.setText(tvm.descriptionProperty().get());
            imgField.setText(tvm.imagePathProperty().get());

            /* Dialog konfigurieren */
            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setTitle("Edit Tour");

            /* Ergebnis auswerten */
            Optional<ButtonType> res = dlg.showAndWait();
            if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                double d; int est;
                try { d = Double.parseDouble(distField.getText().trim()); }
                catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,"Distance must be a number")
                            .showAndWait(); return;
                }
                try { est = Integer.parseInt(estimateField.getText().trim()); }
                catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,"Estimated time must be an integer")
                            .showAndWait(); return;
                }

                /* ViewModel aktualisieren */
                tvm.nameProperty()       .set(nameField.getText().trim());
                tvm.fromProperty()       .set(fromField.getText().trim());
                tvm.toProperty()         .set(toField.getText().trim());
                tvm.distanceProperty()   .set(d);
                tvm.estimatedTimeProperty().set(est);
                tvm.transportTypeProperty().set(transportField.getText().trim());
                tvm.descriptionProperty() .set(descriptionArea.getText().trim());
                tvm.imagePathProperty()   .set(imgField.getText().trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
