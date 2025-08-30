package tourplanner.tourplanner.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.persistence.TourRepository;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
@Service
public class TourServiceImpl implements TourService{
    private final TourRepository tourRepository;

    @Override
    public List<Tour> getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        log.info("Fetched {} tours from the database.", tours.size());
        return tourRepository.findAll();
    }

    @Override
    public void addTour(Tour tour) {
        tourRepository.save(tour);
    }

    @Override
    public void removeTour(Tour tour) {
        tourRepository.delete(tour);
    }
}
