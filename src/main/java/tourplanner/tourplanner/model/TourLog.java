package tourplanner.tourplanner.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class TourLog {
    private final StringProperty tourName = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final IntegerProperty difficulty = new SimpleIntegerProperty();
    private final DoubleProperty totalDistance = new SimpleDoubleProperty();
    private final StringProperty rating = new SimpleStringProperty();

    public TourLog(String tourName, LocalDateTime dt, String comment,
                   int difficulty, double totalDistance, String rating) {
        this.tourName.set(tourName);
        this.dateTime.set(dt);
        this.comment.set(comment);
        this.difficulty.set(difficulty);
        this.totalDistance.set(totalDistance);
        this.rating.set(rating);
    }

    public StringProperty tourNameProperty() {
        return tourName;
    }

    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTime;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public IntegerProperty difficultyProperty() {
        return difficulty;
    }

    public DoubleProperty totalDistanceProperty() {
        return totalDistance;
    }

    public StringProperty ratingProperty() {
        return rating;
    }
}
