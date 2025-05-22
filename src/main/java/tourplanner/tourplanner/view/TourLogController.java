package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.TourLogViewModel;
import tourplanner.tourplanner.model.TourLog;

public class TourLogController {
    @FXML
    private ListView<TourLog> logList;
    private final MainViewModel vm = MainViewModel.getInstance();
    private TourLogViewModel logVM;

    @FXML
    public void initialize() {
        vm.selectedTourProperty().addListener((o, old, sel) -> {
            if (sel != null) {
                logVM = new TourLogViewModel(
                        vm.getLogService(), sel.nameProperty().get()
                );
                logList.setItems(logVM.getLogs());
            } else {
                logList.getItems().clear();
            }
        });
    }

    @FXML
    void onNewLog() { /* analog CreateTourController */ }

    @FXML
    void onDeleteLog() { /* logVM.removeLog(...) */ }
}
