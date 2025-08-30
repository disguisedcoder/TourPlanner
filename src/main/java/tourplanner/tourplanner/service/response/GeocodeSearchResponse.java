package tourplanner.tourplanner.service.response;

import java.util.List;

public record GeocodeSearchResponse(List<GeocodeFeatureResponse> features) {
}
