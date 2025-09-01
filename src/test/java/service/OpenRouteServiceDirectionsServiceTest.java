package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import tourplanner.tourplanner.model.TransportType;
import tourplanner.tourplanner.service.OpenRouteServiceDirectionsService;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenRouteServiceDirectionsServiceTest {

    private OpenRouteServiceDirectionsService newService(RestClient.Builder builder) {
        OpenRouteServiceDirectionsService svc = new OpenRouteServiceDirectionsService(builder);
        ReflectionTestUtils.setField(svc, "baseUrl", "https://api.openrouteservice.org/");
        ReflectionTestUtils.setField(svc, "apiKey", "test-key");
        return svc;
    }

    @Test
    void route_parsesGeoJsonFeatureCollection() {
        // Arrange
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);

        // post() -> uri(Function) -> header -> contentType -> body -> retrieve -> body(String)
        doReturn(uriSpec).when(rest).post();
        doReturn(bodySpec).when(uriSpec).uri(any(Function.class));
        doReturn(bodySpec).when(bodySpec).header(eq("Authorization"), anyString());
        doReturn(bodySpec).when(bodySpec).contentType(eq(MediaType.APPLICATION_JSON));
        doReturn(bodySpec).when(bodySpec).body(any());              // returns RequestBodySpec
        doReturn(resp).when(bodySpec).retrieve();                   // returns ResponseSpec

        String json = """
                {
                  "type":"FeatureCollection",
                  "features":[
                    {
                      "type":"Feature",
                      "properties":{"summary":{"distance":123.4,"duration":3600}},
                      "geometry":{"type":"LineString","coordinates":[[16.37,48.2],[15.44,47.07]]}
                    }
                  ]
                }
                """;
        when(resp.body(String.class)).thenReturn(json);

        var svc = newService(builder);

        // Act
        Optional<OpenRouteServiceDirectionsService.DirectionsResult> opt =
                svc.route(TransportType.DRIVING_CAR, 48.2, 16.37, 47.07, 15.44);

        // Assert
        assertThat(opt).isPresent();
        var r = opt.get();
        assertThat(r.distanceKm()).isEqualTo(123.4);
        assertThat(r.durationMinutes()).isEqualTo(60);
        assertThat(r.geometryGeoJson())
                .isEqualTo("{\"type\":\"LineString\",\"coordinates\":[[16.37,48.2],[15.44,47.07]]}");
    }

    @Test
    void route_parsesRoutesArray_withGeometryObject() {
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);
        doReturn(uriSpec).when(rest).post();
        doReturn(bodySpec).when(uriSpec).uri(any(Function.class));
        doReturn(bodySpec).when(bodySpec).header(eq("Authorization"), anyString());
        doReturn(bodySpec).when(bodySpec).contentType(eq(MediaType.APPLICATION_JSON));
        doReturn(bodySpec).when(bodySpec).body(any());
        doReturn(resp).when(bodySpec).retrieve();

        String json = """
                {
                  "routes":[
                    {
                      "summary":{"distance":42.0,"duration":90},
                      "geometry":{"type":"LineString","coordinates":[[0.0,0.0],[1.0,1.0]]}
                    }
                  ]
                }
                """;
        when(resp.body(String.class)).thenReturn(json);

        var svc = newService(builder);

        var opt = svc.route(TransportType.CYCLING_REGULAR, 0, 0, 1, 1);

        assertThat(opt).isPresent();
        var r = opt.get();
        assertThat(r.distanceKm()).isEqualTo(42.0);
        assertThat(r.durationMinutes()).isEqualTo(2); // 90 s -> 1.5 min -> rundet
        assertThat(r.geometryGeoJson())
                .isEqualTo("{\"type\":\"LineString\",\"coordinates\":[[0.0,0.0],[1.0,1.0]]}");
    }

    @Test
    void route_parsesRoutesArray_withEncodedPolylineSetsNullGeometry() {
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);
        doReturn(uriSpec).when(rest).post();
        doReturn(bodySpec).when(uriSpec).uri(any(Function.class));
        doReturn(bodySpec).when(bodySpec).header(eq("Authorization"), anyString());
        doReturn(bodySpec).when(bodySpec).contentType(eq(MediaType.APPLICATION_JSON));
        doReturn(bodySpec).when(bodySpec).body(any());
        doReturn(resp).when(bodySpec).retrieve();

        String json = """
                {
                  "routes":[
                    {
                      "summary":{"distance":7.5,"duration":30},
                      "geometry":"encPolylineHere"
                    }
                  ]
                }
                """;
        when(resp.body(String.class)).thenReturn(json);

        var svc = newService(builder);

        var opt = svc.route(TransportType.FOOT_WALKING, 0, 0, 1, 1);

        assertThat(opt).isPresent();
        var r = opt.get();
        assertThat(r.distanceKm()).isEqualTo(7.5);
        assertThat(r.durationMinutes()).isEqualTo(1); // 30s ≈ 0.5min -> rundet auf 1
        assertThat(r.geometryGeoJson()).isNull();     // encoded polyline -> null im Service
    }

    @Test
    void route_returnsEmpty_whenClientThrows() {
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);
        doReturn(uriSpec).when(rest).post();
        doReturn(bodySpec).when(uriSpec).uri(any(Function.class));
        doReturn(bodySpec).when(bodySpec).header(eq("Authorization"), anyString());
        doReturn(bodySpec).when(bodySpec).contentType(eq(MediaType.APPLICATION_JSON));
        doReturn(bodySpec).when(bodySpec).body(any());
        doReturn(resp).when(bodySpec).retrieve();

        when(resp.body(String.class)).thenThrow(new RuntimeException("boom"));

        var svc = newService(builder);

        assertThat(svc.route(TransportType.DRIVING_CAR, 0, 0, 1, 1)).isEmpty();
    }
}
