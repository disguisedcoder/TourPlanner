package tourplanner.tourplanner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tourplanner.tourplanner.model.TransportType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenRouteServiceDirectionsService {

    private final RestClient.Builder builder;
    private final ObjectMapper om = new ObjectMapper();

    @Value("${ors.api.base:https://api.openrouteservice.org/}")
    private String baseUrl;

    @Value("${ors.api.key}")
    private String apiKey;

    private RestClient client() { return builder.baseUrl(baseUrl).build(); }

    //     ✅ record statt Lombok @Value – keine @Value-Kollision
    public record DirectionsResult(double distanceKm, int durationMinutes, String geometryGeoJson) {}
    //
//    public Optional<DirectionsResult> route(TransportType profile,
//                                            double fromLat, double fromLng,
//                                            double toLat,   double toLng) {
//        try {
//            var body = Map.of(
//                    "coordinates", new double[][]{ {fromLng, fromLat}, {toLng, toLat} }, // ORS: [lon,lat]
//                    "units", "km",
//                    "instructions", false,
//                    "geometry", "geojson"
//            );
//
//            String json = client().post()
//                    .uri(u -> u.path("/v2/directions/{p}")
//                            .queryParam("api_key", apiKey)   // alternativ: .header("Authorization", apiKey)
//                            .build(profile.getOrsProfile()))
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(body)
//                    .retrieve()
//                    .body(String.class);
//
//            JsonNode feat = om.readTree(json).path("features").get(0);
//            if (feat == null || feat.isMissingNode()) return Optional.empty();
//
//            double km  = feat.path("properties").path("summary").path("distance").asDouble(0.0);
//            double sec = feat.path("properties").path("summary").path("duration").asDouble(0.0);
//            String geom = om.writeValueAsString(feat.path("geometry")); // Geometry-Objekt (LineString)
//
//            return Optional.of(new DirectionsResult(km, (int)Math.round(sec/60.0), geom));
//        } catch (Exception e) {
//            log.warn("ORS directions failed: {}", e.getMessage());
//            return Optional.empty();
//        }
//    }
//    public Optional<DirectionsResult> route(TransportType profile,
//                                            double fromLat, double fromLng,
//                                            double toLat,   double toLng) {
//        try {
//            // ORS erwartet [lon, lat]
//            Map<String, Object> body = new HashMap<>();
//            body.put("coordinates", new double[][]{ {fromLng, fromLat}, {toLng, toLat} });
//            body.put("units", "km");
//            body.put("instructions", false);
//            body.put("geometry", true);               // ✅ boolean, nicht String
//
//            String json = client().post()
//                    .uri(u -> u.path("/v2/directions/{p}")
//                            .queryParam("format", "geojson")  // ✅ GeoJSON-Antwort
//                            .build(profile.getOrsProfile()))
//                    .header("Authorization", apiKey)             // oder .queryParam("api_key", apiKey)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(body)
//                    .retrieve()
//                    .body(String.class);
//
//            JsonNode root = om.readTree(json);
//            JsonNode feat = root.path("features").get(0);
//            if (feat == null || feat.isMissingNode()) {
//                log.warn("ORS directions returned no features for profile={} from=({}, {}) to=({}, {})",
//                        profile, fromLat, fromLng, toLat, toLng);
//                return Optional.empty();
//            }
//
//            double km  = feat.path("properties").path("summary").path("distance").asDouble(0.0);
//            double sec = feat.path("properties").path("summary").path("duration").asDouble(0.0);
//            String geom = om.writeValueAsString(feat.path("geometry")); // GeoJSON Geometry (LineString)
//
//            log.info("ORS directions ok: {} km, {} min, profile={}", km, Math.round(sec/60.0), profile);
//            return Optional.of(new DirectionsResult(km, (int)Math.round(sec/60.0), geom));
//
//        } catch (Exception e) {
//            log.warn("ORS directions failed ({}): profile={} from=({}, {}) to=({}, {})",
//                    e.getMessage(), profile, fromLat, fromLng, toLat, toLng);
//            return Optional.empty();
//        }
//    }
    public Optional<DirectionsResult> route(TransportType profile,
                                            double fromLat, double fromLng,
                                            double toLat,   double toLng) {
        try {
            // ORS erwartet [lon, lat]
            Map<String, Object> body = new java.util.HashMap<>();
            body.put("coordinates", new double[][]{ {fromLng, fromLat}, {toLng, toLat} });
            body.put("units", "km");
            body.put("instructions", false);
            body.put("geometry", true);  // Geometrie anfordern (Format je nach Serverkonfig)

            String json = client().post()
                    .uri(u -> u.path("/v2/directions/{p}").build(profile.getOrsProfile()))
                    .header("Authorization", apiKey)                // oder .queryParam("api_key", apiKey)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            JsonNode root = om.readTree(json);

            // 1) GeoJSON-Antwort (FeatureCollection)?
            JsonNode features = root.path("features");
            if (features.isArray() && features.size() > 0) {
                JsonNode feat = features.get(0);
                double km  = feat.path("properties").path("summary").path("distance").asDouble(0.0);
                double sec = feat.path("properties").path("summary").path("duration").asDouble(0.0);
                String geom = om.writeValueAsString(feat.path("geometry")); // Geometry-Objekt (LineString)

                log.info("ORS directions ok[geojson]: {} km, {} min, profile={}", km, Math.round(sec/60.0), profile);
                return Optional.of(new DirectionsResult(km, (int)Math.round(sec/60.0), geom));
            }

            // 2) Klassische JSON-Antwort mit routes[0]?
            JsonNode routes = root.path("routes");
            if (routes.isArray() && routes.size() > 0) {
                JsonNode r0 = routes.get(0);
                double km  = r0.path("summary").path("distance").asDouble(0.0);
                double sec = r0.path("summary").path("duration").asDouble(0.0);

                // Geometrie kann Objekt ODER String sein
                JsonNode geomNode = r0.path("geometry");
                String geomJson = null;
                if (geomNode.isObject()) {
                    // bereits GeoJSON-Geometry
                    geomJson = om.writeValueAsString(geomNode);
                } else if (geomNode.isTextual()) {
                    // encoded polyline -> für jetzt ohne Linie anzeigen (später decoden)
                    log.info("ORS geometry is encoded polyline (string) – skipping route line for now.");
                }

                log.info("ORS directions ok[routes]: {} km, {} min, profile={}", km, Math.round(sec/60.0), profile);
                return Optional.of(new DirectionsResult(km, (int)Math.round(sec/60.0), geomJson));
            }

            // Nichts erkannt → Debug-Ausgabe
            log.warn("ORS directions: unknown response shape, raw={}", json);
            return Optional.empty();

        } catch (Exception e) {
            log.warn("ORS directions failed ({}): profile={} from=({}, {}) to=({}, {})",
                    e.getMessage(), profile, fromLat, fromLng, toLat, toLng);
            return Optional.empty();
        }
    }
}
