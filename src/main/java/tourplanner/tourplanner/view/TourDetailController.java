package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourLogViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

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
    @FXML private WebView mapView;

    // --- NEU: Logs-Bereich
    @FXML private TableView<TourLogViewModel> logsTable;
    @FXML private TableColumn<TourLogViewModel, LocalDate> colDate;
    @FXML private TableColumn<TourLogViewModel, String> colUser;
    @FXML private TableColumn<TourLogViewModel, String> colComment;
    @FXML private TableColumn<TourLogViewModel, Number> colDiff;
    @FXML private TableColumn<TourLogViewModel, Number> colTime;
    @FXML private TableColumn<TourLogViewModel, Number> colDist;
    @FXML private TableColumn<TourLogViewModel, Number> colRating;
    @FXML private Button btnEditLog, btnDeleteLog;

    private final MainViewModel vm;
    private final LogDialogController logDialog;
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
                transportField.textProperty().bind(
                        Bindings.createStringBinding(
                                () -> {
                                    var tt = n.getTransportTypeProperty().get();
                                    return tt != null ? tt.getLabel() : "";  // oder tt.name() falls du den Enum-Namen willst
                                },
                                n.getTransportTypeProperty()
                        )
                );                estimateField.textProperty().bind(
                        Bindings.concat(n.getEstimatedTimeProperty()));
                try {
                    URL url = getClass().getResource("/leaflet.html");
                    if (url == null) throw new IOException("leaflet.html not found in resources.");

                    String content = Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);

                    // GeoJSON-Punkte: [lng, lat]  (lon, lat)
                    String pointA = """
        {"type":"Feature","geometry":{"type":"Point","coordinates":[%s,%s]}}
        """.formatted(n.getFromLngProperty().get(), n.getFromLatProperty().get());

                    String pointB = """
        {"type":"Feature","geometry":{"type":"Point","coordinates":[%s,%s]}}
        """.formatted(n.getToLngProperty().get(), n.getToLatProperty().get());

                    content = content.replace("{GEOJSON_1}", pointA);
                    content = content.replace("{GEOJSON_2}", pointB);

                    // CENTER: nur wenn dein leaflet.html es für setView(CENTER, …) verwendet -> [lat, lng]
                    content = content.replace("{CENTER}",
                            "[%s,%s]".formatted(n.getFromLatProperty().get(), n.getFromLngProperty().get()));

                    // ROUTE einsetzen (Geometry-Objekt oder null)
                    String route = (n.getRouteGeoJsonProperty() != null) ? n.getRouteGeoJsonProperty().get() : null;
                    content = content.replace("{ROUTE_GEOJSON}", (route == null || route.isBlank()) ? "null" : route);

                    mapView.getEngine().loadContent(content, "text/html");
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }


            } else {
                nameField.clear(); fromField.clear(); toField.clear(); distField.clear();
                descriptionArea.clear(); transportField.clear(); estimateField.clear();
            }
            // Logs-Tabelle an ViewModel binden
            if (logsTable != null) {
                colDate.setCellValueFactory(c -> c.getValue().getDateProperty());
                colUser.setCellValueFactory(c -> c.getValue().getUsernameProperty());
                colComment.setCellValueFactory(c -> c.getValue().getCommentProperty());
                colDiff.setCellValueFactory(c -> c.getValue().getDifficultyProperty());
                colTime.setCellValueFactory(c -> c.getValue().getTotalTimeProperty());
                colDist.setCellValueFactory(c -> c.getValue().getTotalDistanceProperty());
                colRating.setCellValueFactory(c -> c.getValue().getRatingProperty());

                logsTable.setItems(vm.logs);

                // Buttons aktivieren/deaktivieren je nach Auswahl
                logsTable.getSelectionModel().selectedItemProperty().addListener((observer, old, neu) -> {
                    boolean has = neu != null;
                    btnEditLog.setDisable(!has);
                    btnDeleteLog.setDisable(!has);
                });
            }
        });
    }
    @FXML private void onAddLog() {
        if (vm.selectedTourProperty().get() == null) {
            new Alert(Alert.AlertType.WARNING, "Bitte zuerst eine Tour auswählen.").showAndWait();
            return;
        }
        logDialog.showAddDialog();
    }

    @FXML private void onEditLog() {
        var sel = logsTable.getSelectionModel().getSelectedItem();
        if (sel != null) logDialog.showEditDialog(sel);
    }

    @FXML private void onDeleteLog() {
        var sel = logsTable.getSelectionModel().getSelectedItem();
        if (sel != null) vm.deleteLog(sel);
    }
}
