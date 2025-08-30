package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.File;
import java.net.URL;

@RequiredArgsConstructor

@Controller
public class TourDetailController {
    @FXML private TextField nameField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField distField;
    @FXML private TextArea  descriptionArea;
    @FXML private TextField transportField;
    @FXML private TextField estimateField;

    private final MainViewModel vm;
    private TourViewModel lastBound;

    @FXML
    public void initialize() {

        vm.selectedTourProperty().addListener((obs, o, n) -> {
            if (lastBound != null) {
                nameField.textProperty().unbind();
                fromField.textProperty().unbind();
                toField.textProperty().unbind();
                distField.textProperty().unbind();
                descriptionArea.textProperty().unbind();
                transportField.textProperty().unbind();
                estimateField.textProperty().unbind();
            }
            lastBound = n;
            if (n != null) {
                nameField.textProperty().bind(n.getNameProperty());
                fromField.textProperty().bind(n.getFromProperty());
                toField.textProperty().bind(n.getToProperty());
                distField.textProperty().bind(
                        Bindings.concat(n.getDistanceProperty()));
                descriptionArea.textProperty().bind(n.getDescriptionProperty());
                transportField.textProperty().bind(n.getTransportTypeProperty());
                estimateField.textProperty().bind(
                        Bindings.concat(n.getEstimatedTimeProperty()));

            } else {
                nameField.clear(); fromField.clear(); toField.clear(); distField.clear();
                descriptionArea.clear(); transportField.clear(); estimateField.clear();
            }
        });
    }
}
