package tourplanner.tourplanner.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.persistence.TourRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class TourServiceImpl implements TourService{
    private final TourRepository tourRepository;

    @Override
    public List<Tour> getAllTours() {
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
