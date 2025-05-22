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

public class CreateTourController {
    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextField estimateField;
    @FXML private TextField transportField;
    @FXML private TextArea  descriptionArea;
    @FXML private TextField imgField;

    private final MainViewModel vm = MainViewModel.getInstance();

    public void showDialog() {
        try {
            FXMLLoader f = new FXMLLoader(
                    getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml"));
            URL url = getClass().getResource(
                    "/tourplanner/tourplanner/view/CreateTour.fxml");
            f.setController(this);
            Region content = f.load();

            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes()
                    .addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setTitle("Create Tour");

            Optional<ButtonType> res = dlg.showAndWait();
            if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                double d; int est;
                try { d = Double.parseDouble(distField.getText()); }
                catch (Exception e) { new Alert(Alert.AlertType.ERROR,
                        "Distance must be a number").showAndWait(); return; }
                try { est = Integer.parseInt(estimateField.getText()); }
                catch (Exception e) { new Alert(Alert.AlertType.ERROR,
                        "Estimated time must be an integer").showAndWait(); return; }

                String img = imgField.getText().isBlank()
                        ? "/tourplanner/tourplanner/view/images/demo.png" : imgField.getText();

                Tour model = new Tour(
                        nameField.getText(),
                        fromField.getText(),
                        toField.getText(),
                        d,
                        est,
                        transportField.getText(),
                        descriptionArea.getText(),
                        img
                );
                vm.addTour(new TourViewModel(model));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
