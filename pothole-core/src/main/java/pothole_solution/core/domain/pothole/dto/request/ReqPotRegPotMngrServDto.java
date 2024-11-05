package pothole_solution.core.domain.pothole.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.Random;

@Getter
@NoArgsConstructor
public class ReqPotRegPotMngrServDto {
    @NotNull(message = "위도의 값은 반드시 존재해야 합니다.")
    private double lat;

    @NotNull(message = "경도의 값은 반드시 존재해야 합니다.")
    private double lon;

    @Builder
    public ReqPotRegPotMngrServDto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Pothole toPothole() {
        GeometryFactory geometryFactory = new GeometryFactory();

        Integer randomImportance = new Random().nextInt(101);
        Integer randomDangerous = new Random().nextInt(101);

        return Pothole.builder()
                .roadName("강남로 1")
                .point(geometryFactory.createPoint(new Coordinate(lon, lat)))
                .importance(randomImportance)
                .dangerous(randomDangerous)
                .processStatus(Progress.REGISTER)
                .build();
    }
}