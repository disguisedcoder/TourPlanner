package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;

@RequiredArgsConstructor
@Controller
public class SidebarController {

    private final MainViewModel vm;

    @FXML
    private TextField searchField;

    @FXML
    private void initialize() {
        // Einweg-Bindung: UI -> VM
        vm.searchQueryProperty().bind(searchField.textProperty());

        // Beim Start einmal laden (falls nicht schon woanders getan)
        vm.loadTours();
    }
}