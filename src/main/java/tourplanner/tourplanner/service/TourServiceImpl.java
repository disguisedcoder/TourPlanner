package tourplanner.tourplanner.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.persistence.TourRepository;
import tourplanner.tourplanner.service.response.GeocodeSearchResponse;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
@Service
public class TourServiceImpl implements TourService{
    private final TourRepository tourRepository;
    private final OpenRouteServiceGeocodeSearchService geocodeSearchService;

    @Override
    public List<Tour> getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        log.info("Fetched {} tours from the database.", tours.size());
        return tourRepository.findAll();
    }

    @Override
    public void addTour(Tour tour) {
        GeocodeSearchResponse fromSearchResponse = geocodeSearchService.getCoordinates(tour.getFromLocation());
        GeocodeSearchResponse toSearchResponse = geocodeSearchService.getCoordinates(tour.getToLocation());

        tour.setFromLng(fromSearchResponse.features().get(0).geometry().coordinates().get(1));
        tour.setFromLat(fromSearchResponse.features().get(0).geometry().coordinates().get(0));
        tour.setToLng(toSearchResponse.features().get(0).geometry().coordinates().get(1));
        tour.setToLat(toSearchResponse.features().get(0).geometry().coordinates().get(0));


        tourRepository.save(tour);
        log.info("Added new tour: {}", tour.getName());
    }

    @Override
    public void removeTour(Tour tour) {

        if (tour.getId() == 0L) {
            throw new IllegalArgumentException("Tour ID required for deletion");
        }
        // deleteById ist robust und vermeidet ein unnötiges merge()
        tourRepository.deleteById(tour.getId());
    }
}
