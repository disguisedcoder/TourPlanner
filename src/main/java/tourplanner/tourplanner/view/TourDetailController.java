package tourplanner.tourplanner.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tourplanner.tourplanner.viewmodel.MainViewModel;
import tourplanner.tourplanner.viewmodel.model.TourViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
//                try {
//                    URL url = getClass().getResource("/leaflet.html");
//                    if(url == null) throw new IOException("leaflet.html not found in resources.");
//
//                    String content = Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);
//                    content = content.replace("{GEOJSON_1}", "{\"type\": \"Feature\", \"geometry\": { \"type\": \"Point\", \"coordinates\": [%s, %s] }};"
//                            .formatted(n.getFromLatProperty().get(), n.getFromLngProperty().get()));
//                    content = content.replace("{GEOJSON_2}", "{\"type\": \"Feature\", \"geometry\": { \"type\": \"Point\", \"coordinates\": [%s, %s] }};"
//                            .formatted(n.getToLatProperty().get(), n.getToLngProperty().get()));
//                    content = content.replace("{CENTER}", "[%s, %s]".formatted(n.getFromLngProperty().get(), n.getFromLatProperty().get()));
//
//                    mapView.getEngine().loadContent(content, "text/html");
//                } catch(IOException | URISyntaxException e) {
//                    e.printStackTrace();
//                }

            } else {
                nameField.clear(); fromField.clear(); toField.clear(); distField.clear();
                descriptionArea.clear(); transportField.clear(); estimateField.clear();
            }
        });
    }
}
