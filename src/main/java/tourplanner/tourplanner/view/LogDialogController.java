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
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class LogDialogController {

    @FXML private DatePicker dateField;
    @FXML private TextField  userField;
    @FXML private TextArea   commentArea;
    @FXML private Spinner<Integer> difficultySpinner;
    @FXML private TextField  timeField;
    @FXML private TextField  distanceField;
    @FXML private Spinner<Integer> ratingSpinner;

    private final MainViewModel vm;

    public void showAddDialog() { showDialogInternal(null); }
    public void showEditDialog(TourLogViewModel existing) { showDialogInternal(existing); }

    private void showDialogInternal(TourLogViewModel existing) {
        FXMLLoader f = new FXMLLoader(getClass().getResource("/tourplanner/tourplanner/view/LogDialog.fxml"));
        f.setController(this);
        Region content;
        try { content = f.load(); } catch (IOException e) { throw new RuntimeException(e); }

        difficultySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));

        if (existing != null) {
            dateField.setValue(existing.getDateProperty().get());
            userField.setText(nz(existing.getUsernameProperty().get()));
            commentArea.setText(nz(existing.getCommentProperty().get()));
            difficultySpinner.getValueFactory().setValue(existing.getDifficultyProperty().get());
            timeField.setText(String.valueOf(existing.getTotalTimeProperty().get()));
            distanceField.setText(String.valueOf(existing.getTotalDistanceProperty().get()));
            ratingSpinner.getValueFactory().setValue(existing.getRatingProperty().get());
        } else {
            dateField.setValue(LocalDate.now());
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle(existing == null ? "Add Log" : "Edit Log");
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final Button okBtn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> { if (!validate()) ev.consume(); });

        Optional<ButtonType> res = dlg.showAndWait();
        if (res.orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        if (existing == null) {
            TourLogViewModel vmLog = new TourLogViewModel();
            writeBack(vmLog);
            vm.addLog(vmLog);
        } else {
            writeBack(existing);
            vm.updateLog(existing);
        }
    }

    private void writeBack(TourLogViewModel t) {
        t.getDateProperty().set(dateField.getValue());
        t.getUsernameProperty().set(ztrim(userField.getText()));
        t.getCommentProperty().set(ztrim(commentArea.getText()));
        t.getDifficultyProperty().set(difficultySpinner.getValue());
        t.getRatingProperty().set(ratingSpinner.getValue());

        int mins = 0;
        String tf = ztrim(timeField.getText());
        if (!tf.isBlank()) try { mins = Integer.parseInt(tf); } catch (Exception ignore) {}
        t.getTotalTimeProperty().set(mins);

        double km = 0.0;
        String df = ztrim(distanceField.getText());
        if (!df.isBlank()) try { km = Double.parseDouble(df); } catch (Exception ignore) {}
        t.getTotalDistanceProperty().set(km);
    }

    private boolean validate() {
        if (dateField.getValue() == null) return err("Bitte Datum wählen.");
        if (ztrim(commentArea.getText()).isBlank()) return err("Kommentar darf nicht leer sein.");

        try {
            int mins = Integer.parseInt(ztrim(timeField.getText()));
            if (mins < 0) return err("Zeit (Minuten) darf nicht negativ sein.");
        } catch (Exception e) { return err("Zeit (Minuten) muss eine Zahl sein."); }

        String df = ztrim(distanceField.getText());
        if (!df.isBlank()) {
            try { if (Double.parseDouble(df) < 0) return err("Distanz (km) darf nicht negativ sein."); }
            catch (Exception e) { return err("Distanz (km) muss eine Zahl sein."); }
        }

        int d = difficultySpinner.getValue();
        if (d < 1 || d > 5) return err("Difficulty muss 1..5 sein.");

        int r = ratingSpinner.getValue();
        if (r < 1 || r > 5) return err("Rating muss 1..5 sein.");
        return true;
    }

    private boolean err(String m) { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); return false; }
    private static String nz(String s){ return s==null ? "" : s; }
    private static String ztrim(String s){ return s==null ? "" : s.trim(); }
}
