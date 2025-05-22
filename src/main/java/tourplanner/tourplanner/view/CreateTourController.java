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

public class CreateTourController {
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField transportField;
    @FXML private TextField distField;
    @FXML private TextField timeField;
    @FXML private TextField imgField;  // bleibt hier, wird aber nicht mehr ausgewertet

    private final MainViewModel vm = MainViewModel.getInstance();

    public CreateTourController() { }

    public void showDialog() {
        try {
            URL url = getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            Region content = loader.load();

            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setTitle("Create Tour");

            Optional<ButtonType> res = dlg.showAndWait();
            if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                double d;
                try {
                    d = Double.parseDouble(distField.getText().trim());
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Distance must be a number").showAndWait();
                    return;
                }
                // imagePath leer übergeben → TourViewModel setzt demo.png
                Tour model = new Tour(
                        nameField.getText().trim(),
                        descriptionField.getText().trim(),
                        fromField.getText().trim(),
                        toField.getText().trim(),
                        transportField.getText().trim(),
                        d,
                        timeField.getText().trim(),
                        ""
                );
                vm.addTour(new TourViewModel(model));
                // Felder zurücksetzen
                nameField.clear();
                descriptionField.clear();
                fromField.clear();
                toField.clear();
                transportField.clear();
                distField.clear();
                timeField.clear();
                imgField.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
