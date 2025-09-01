package tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String username;
    private LocalDateTime dateTime;
    private String comment;
    private int difficulty;
    private double totalDistance;
    private int totalTime;
    private int rating;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_tour"))
    private Tour tour;

    public TourLog(String tourName,String username , LocalDateTime now, String comment, int difficulty, double dist, int totalTime, int rating) {
        this.tourName = tourName;
        this.username = username;
        this.dateTime = now;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = dist;
        this.totalTime = totalTime;
        this.rating = rating;
    }
}
