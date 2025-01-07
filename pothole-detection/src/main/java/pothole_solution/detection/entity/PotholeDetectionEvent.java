package pothole_solution.detection.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Entity(name = "POTHOLE_DETECTION_EVENT")
@Table(name = "POTHOLE_DETECTION_EVENT")
@Getter
@AllArgsConstructor
public class PotholeDetectionEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long id;

    @Column
    private Long messageId;

    @Column(name = "LAT")
    private Double lat;

    @Column(name = "LON")
    private Double lon;

    @Column(name = "IMPORTANCE")
    private Integer importance;

    @Column(name = "DANGER")
    private Integer danger;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Builder
    public PotholeDetectionEvent(@NotNull Long messageId, @NotNull Double lat, @NotNull Double lon, @NotNull Integer importance, @NotNull Integer danger, @NotNull String fileName) {
        this.messageId = messageId;
        this.lat = lat;
        this.lon = lon;
        this.importance = importance;
        this.danger = danger;
        this.fileName = fileName;
    }
}
