package tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "tour")
    private List<TourLog> tourLogs;


    @Builder
    public Tour(String name, String fromLocation, String toLocation, double distance, String description, int estimatedTime, String transportType) {
        this.name = name;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distance = distance;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.transportType = transportType;
    }
}