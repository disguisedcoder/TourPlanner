package tourplanner.tourplanner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tourplanner.tourplanner.service.response.GeocodeSearchResponse;


@Service
@Slf4j
public class OpenRouteServiceGeocodeSearchService {
    private final RestClient restClient;

    @Value("${ors.api.base:https://api.openrouteservice.org/}")
    private String baseUrl;

    @Value("${ors.api.key}")

    private String apiKey;

    public OpenRouteServiceGeocodeSearchService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.openrouteservice.org/")
                .build();
    }

    public GeocodeSearchResponse getCoordinates(String address) {
        log.debug("Trying to get the coordinate of address {}", address);

        GeocodeSearchResponse response = this.restClient.get().uri(uriBuilder -> uriBuilder
                        .path("geocode/search")
                        .queryParam("api_key", apiKey)
                        .queryParam("text", address)
                        .queryParam("size", 1)
                        .build())
                .retrieve()
                .body(GeocodeSearchResponse.class);

        if (response == null || response.features() == null || response.features().isEmpty()) {
            throw new IllegalArgumentException(String.format("Could not find address %s", address));
        }

        return response;
    }
}
