package tourplanner.tourplanner.viewmodel.model;

import javafx.beans.property.*;
import lombok.Getter;
import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.model.TourLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class TourLogViewModel {

    private Long id;

    private final StringProperty  usernameProperty      = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final StringProperty  commentProperty       = new SimpleStringProperty();
    private final IntegerProperty difficultyProperty    = new SimpleIntegerProperty();
    private final DoubleProperty  totalDistanceProperty = new SimpleDoubleProperty();
    private final IntegerProperty totalTimeProperty     = new SimpleIntegerProperty();
    private final IntegerProperty ratingProperty        = new SimpleIntegerProperty();

    public TourLogViewModel() {}

    public TourLogViewModel(TourLog log) {
        this.id = log.getId();
        this.usernameProperty.set(log.getUsername());
        this.dateProperty.set(log.getDateTime() == null ? null : log.getDateTime().toLocalDate());
        this.commentProperty.set(log.getComment());
        this.difficultyProperty.set(log.getDifficulty());
        this.totalDistanceProperty.set(log.getTotalDistance());
        this.totalTimeProperty.set(log.getTotalTime());
        this.ratingProperty.set(log.getRating());
    }

    public TourLog toModel(Tour tour) {
        TourLog l = new TourLog();
        if (id != null) l.setId(id);
        l.setTour(tour);
        l.setTourName(tour.getName());
        l.setUsername(usernameProperty.get());
        LocalDate d = dateProperty.get();
        l.setDateTime(d == null ? null : LocalDateTime.of(d, LocalTime.NOON));
        l.setComment(commentProperty.get());
        l.setDifficulty(difficultyProperty.get());
        l.setTotalDistance(totalDistanceProperty.get());
        l.setTotalTime(totalTimeProperty.get());
        l.setRating(ratingProperty.get());
        return l;
    }
}
