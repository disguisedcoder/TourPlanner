package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import tourplanner.tourplanner.service.OpenRouteServiceGeocodeSearchService;
import tourplanner.tourplanner.service.response.Feature;
import tourplanner.tourplanner.service.response.GeocodeSearchResponse;
import tourplanner.tourplanner.service.response.GeometryResponse;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenRouteServiceGeocodeSearchServiceTest {

    @Test
    void getCoordinates_returnsResponse_whenApiReturnsFeature() {
        // Arrange RestClient chain
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec<?> uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder); // << disambig
        when(builder.build()).thenReturn(rest);

        doReturn(uriSpec).when(rest).get();
        // overload-sicher: uri(Function<UriBuilder, URI>)
        doReturn(headersSpec).when(uriSpec).uri(any(Function.class));
        doReturn(resp).when(headersSpec).retrieve();

        GeocodeSearchResponse expected = new GeocodeSearchResponse(
                List.of(new Feature(new GeometryResponse(List.of(16.3738, 48.2082))))
        );
        when(resp.body(GeocodeSearchResponse.class)).thenReturn(expected);

        OpenRouteServiceGeocodeSearchService svc = new OpenRouteServiceGeocodeSearchService(builder);
        ReflectionTestUtils.setField(svc, "apiKey", "test-key");

        // Act
        GeocodeSearchResponse got = svc.getCoordinates("Wien");

        // Assert
        assertThat(got).isNotNull();
        assertThat(got.features()).hasSize(1);
        assertThat(got.features().get(0).geometry().coordinates())
                .containsExactly(16.3738, 48.2082);

        verify(rest).get();
        verify(uriSpec).uri(any(Function.class));
        verify(headersSpec).retrieve();
        verify(resp).body(GeocodeSearchResponse.class);
    }

    @Test
    void getCoordinates_throws_whenResponseIsNull() {
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec<?> uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);
        doReturn(uriSpec).when(rest).get();
        doReturn(headersSpec).when(uriSpec).uri(any(Function.class));
        doReturn(resp).when(headersSpec).retrieve();
        when(resp.body(GeocodeSearchResponse.class)).thenReturn(null);

        OpenRouteServiceGeocodeSearchService svc = new OpenRouteServiceGeocodeSearchService(builder);
        ReflectionTestUtils.setField(svc, "apiKey", "test-key");

        assertThatThrownBy(() -> svc.getCoordinates("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Could not find address");
    }

    @Test
    void getCoordinates_throws_whenFeaturesEmpty() {
        RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_SELF);
        RestClient rest = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec<?> uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec resp = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(rest);
        doReturn(uriSpec).when(rest).get();
        doReturn(headersSpec).when(uriSpec).uri(any(Function.class));
        doReturn(resp).when(headersSpec).retrieve();

        when(resp.body(GeocodeSearchResponse.class)).thenReturn(new GeocodeSearchResponse(List.of()));

        OpenRouteServiceGeocodeSearchService svc = new OpenRouteServiceGeocodeSearchService(builder);
        ReflectionTestUtils.setField(svc, "apiKey", "test-key");

        assertThatThrownBy(() -> svc.getCoordinates("leer"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Could not find address");
    }
}
