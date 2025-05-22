package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class EditTourController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField fromField;
    @FXML
    private TextField toField;
    @FXML
    private TextField transportField;
    @FXML
    private TextField distField;
    @FXML
    private TextField timeField;
    // kein imgField mehr

    private final MainViewModel vm = MainViewModel.getInstance();
    private TourViewModel current;

    /**
     * Lädt den Dialog, füllt die Felder und speichert bei OK direkt in die Properties
     */
    public void showDialog(TourViewModel tvm) {
        this.current = tvm;
        try {
            URL url = getClass().getResource("/tourplanner/tourplanner/view/EditTour.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            Region content = loader.load();

            // Fülle die TextFields aus dem ViewModel
            nameField.setText(current.nameProperty().get());
            descriptionField.setText(current.descriptionProperty().get());
            fromField.setText(current.fromProperty().get());
            toField.setText(current.toProperty().get());
            transportField.setText(current.transportTypeProperty().get());
            distField.setText(String.valueOf(current.distanceProperty().get()));
            timeField.setText(current.estimatedTimeProperty().get());

            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setTitle("Edit Tour");

            if (dlg.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                double d;
                try {
                    d = Double.parseDouble(distField.getText().trim());
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Distance must be a number").showAndWait();
                    return;
                }
                // Schreibe Änderungen direkt in das bestehende ViewModel
                current.nameProperty().set(nameField.getText().trim());
                current.descriptionProperty().set(descriptionField.getText().trim());
                current.fromProperty().set(fromField.getText().trim());
                current.toProperty().set(toField.getText().trim());
                current.transportTypeProperty().set(transportField.getText().trim());
                current.distanceProperty().set(d);
                current.estimatedTimeProperty().set(timeField.getText().trim());
                // imagePathProperty bleibt unverändert

                // Persistiere die Änderungen
                vm.updateTour(current);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
