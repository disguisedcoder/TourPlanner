package tourplanner.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String fromLocation;
    private String toLocation;
    private double distance;
    private String description;
    private int estimatedTime;
    private String transportType;
    private String imagePath;

    @Builder
    public Tour(String name, String fromLocation, String toLocation, double distance, String description, int estimatedTime, String transportType, String imagePath) {
        this.name = name;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distance = distance;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.transportType = transportType;
        this.imagePath = imagePath;
    }
}