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
    @FXML
    private TextField nameField, fromField, toField, distField, imgField;
    private final MainViewModel vm = MainViewModel.getInstance();

    // Default-Konstruktor wird automatisch genutzt
    public CreateTourController() {
    }

    public void showDialog() {
        try {
            FXMLLoader f = new FXMLLoader(
                    getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml")
            );
            URL url = getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml");
            System.out.println("FXML-URL: " + url);

            f.setController(this);
            Region content = f.load();

            Dialog<ButtonType> dlg = new Dialog<>();
            dlg.getDialogPane().setContent(content);
            dlg.getDialogPane().getButtonTypes()
                    .addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setTitle("Create Tour");

            Optional<ButtonType> res = dlg.showAndWait();
            if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                double d;
                try {
                    d = Double.parseDouble(distField.getText());
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Distance must be a number")
                            .showAndWait();
                    return;
                }
                var model = new Tour(
                        nameField.getText(),
                        fromField.getText(),
                        toField.getText(),
                        d,
                        imgField.getText()
                );
                vm.addTour(new TourViewModel(model));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
