package tourplanner.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tourName;
    private LocalDateTime dateTime;
    private String comment;
    private int difficulty;
    private double totalDistance;
    private int totalTime;
    private String rating;

    public TourLog(String tourName, LocalDateTime now, String comment, int difficulty, double dist, int totalTime, String rating) {
        this.tourName = tourName;
        this.dateTime = now;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = dist;
        this.totalTime = totalTime;
        this.rating = rating;
    }
}
