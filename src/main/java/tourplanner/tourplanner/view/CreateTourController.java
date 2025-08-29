package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourViewModel;

import java.io.IOException;
import java.net.URL;
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
    @FXML private TextField transportField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField imgField;

    private final MainViewModel vm;

    public void showDialog() {
        FXMLLoader f = new FXMLLoader(getClass().getResource("/tourplanner/tourplanner/view/CreateTour.fxml"));

        f.setController(this);
        Region content = null;
        try {
            content = f.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dlg.setTitle("Create Tour");

        Optional<ButtonType> res = dlg.showAndWait();

        if (res.orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                Double.parseDouble(distField.getText());
            }
            catch (Exception e) { new Alert(Alert.AlertType.ERROR,
                    "Distance must be a number").showAndWait(); return; }
            try {
                Integer.parseInt(estimateField.getText());
            }
            catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Estimated time must be an integer").showAndWait();
                return;
            }

            String img = imgField.getText().isBlank()
                    ? "/tourplanner/tourplanner/view/images/demo.png" : imgField.getText();

            TourViewModel model = new TourViewModel(
                    nameField.textProperty(),
                    fromField.textProperty(),
                    toField.textProperty(),
                    distField.textProperty(),
                    imgField.textProperty(),
                    descriptionArea.textProperty(),
                    transportField.textProperty(),
                    estimateField.textProperty()
            );

            vm.addTour(model);
        }
    }
}
