package pothole_solution.core.domain.pothole.dto.message.creation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pothole_solution.core.domain.pothole.dto.request.ReqPotRegPotMngrServDto;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PotholeCreationContent {
    private Double lat;
    private Double lon;
    private Float importance;
    private Float danger;
    private String imageLink;

    public ReqPotRegPotMngrServDto toCreationDto() {
        return new ReqPotRegPotMngrServDto(this.lat, this.lon, this.importance.intValue(), this.danger.intValue());
    }
}
