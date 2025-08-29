package tourplanner.tourplanner.viewmodel.model;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TourViewModel {
    private TextField nameField;
    private TextField fromField;
    private TextField toField;
    private TextField distField;
    private TextField estimateField;
    private TextField transportField;
    private TextArea descriptionArea;
    private TextField imgField;
}
