package pothole_solution.detection.service.api.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CheckPotholeResponseDto {
    private Boolean isPothole;
    private int danger;
    private int importance;
    private String[] segments;
}
