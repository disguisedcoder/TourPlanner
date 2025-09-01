// src/main/java/tourplanner/tourplanner/view/ManageLogsController.java
package tourplanner.tourplanner.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourLogViewModel;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class ManageLogsController {

    @FXML private TableView<TourLogViewModel> logsTable;
    @FXML private TableColumn<TourLogViewModel, java.time.LocalDate> colDate;
    @FXML private TableColumn<TourLogViewModel, String> colUser;
    @FXML private TableColumn<TourLogViewModel, String> colComment;
    @FXML private TableColumn<TourLogViewModel, Number> colDiff;
    @FXML private TableColumn<TourLogViewModel, Number> colTime;
    @FXML private TableColumn<TourLogViewModel, Number> colDist;
    @FXML private TableColumn<TourLogViewModel, Number> colRating;

    private final MainViewModel vm;
    private final LogDialogController logDialog;

    public void showDialog() {
        FXMLLoader f = new FXMLLoader(getClass().getResource("/tourplanner/tourplanner/view/ManageLogs.fxml"));
        f.setController(this);
        Region content;
        try { content = f.load(); } catch (IOException e) { throw new RuntimeException(e); }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Manage Logs");
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // TableView init
        colDate.setCellValueFactory(c -> c.getValue().getDateProperty());
        colUser.setCellValueFactory(c -> c.getValue().getUsernameProperty());
        colComment.setCellValueFactory(c -> c.getValue().getCommentProperty());
        colDiff.setCellValueFactory(c -> c.getValue().getDifficultyProperty());
        colTime.setCellValueFactory(c -> c.getValue().getTotalTimeProperty());
        colDist.setCellValueFactory(c -> c.getValue().getTotalDistanceProperty());
        colRating.setCellValueFactory(c -> c.getValue().getRatingProperty());

        logsTable.setItems(vm.logs);

        dlg.showAndWait();
    }

    @FXML private void onAdd()  { logDialog.showAddDialog(); }
    @FXML private void onEdit() {
        var sel = logsTable.getSelectionModel().getSelectedItem();
        if (sel != null) logDialog.showEditDialog(sel);
    }
    @FXML private void onDelete() {
        var sel = logsTable.getSelectionModel().getSelectedItem();
        if (sel != null) vm.deleteLog(sel);
    }
}
