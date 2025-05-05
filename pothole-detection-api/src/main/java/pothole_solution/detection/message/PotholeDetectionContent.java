package pothole_solution.detection.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PotholeDetectionContent {
    private Double lat;
    private Double lon;
    private String video;
}
