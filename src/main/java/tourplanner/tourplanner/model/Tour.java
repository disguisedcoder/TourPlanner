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

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    private double fromLat;
    private double fromLng;
    private double toLat;
    private double toLng;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "tour")
    private List<TourLog> tourLogs;


    @Builder
    public Tour(String name, String fromLocation, String toLocation, double distance, String description, int estimatedTime, TransportType transportType) {
        this.name = name;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distance = distance;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.transportType = transportType;
    }
}