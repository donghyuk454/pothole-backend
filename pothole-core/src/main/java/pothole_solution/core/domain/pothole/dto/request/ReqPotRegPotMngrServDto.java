package pothole_solution.core.domain.pothole.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqPotRegPotMngrServDto {
    @NotNull(message = "위도의 값은 반드시 존재해야 합니다.")
    private double lat;

    @NotNull(message = "경도의 값은 반드시 존재해야 합니다.")
    private double lon;

    @NotNull(message = "중요도 값은 반드시 존재해야 합니다.")
    private int importance;

    @NotNull(message = "위험도 값은 반드시 존재해야 합니다.")
    private int dangerous;

    public Pothole toPothole() {
        GeometryFactory geometryFactory = new GeometryFactory();

        return Pothole.builder()
                .point(geometryFactory.createPoint(new Coordinate(lon, lat)))
                .importance(importance)
                .dangerous(dangerous)
                .processStatus(Progress.REGISTER)
                .build();
    }
}