package pothole_solution.core.domain.pothole.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;

@Getter
@NoArgsConstructor
public class RespPotSimInfoPotMngrCntrDto {
    private Long potholeId;
    private double lat;
    private double lon;
    private String roadAddress;
    private String roadName;            // 도로명
    private String roadNumber;          // 도로 번호
    private String zipCode;             // 우편 번호
    private String thumbnail;
    private Integer importance;
    private Integer dangerous;
    private Progress progressStatus;

    @Builder
    public RespPotSimInfoPotMngrCntrDto(Pothole pothole) {
        this.potholeId = pothole.getPotholeId();
        this.lat = pothole.getPoint().getY();
        this.lon = pothole.getPoint().getX();
        this.roadName = pothole.getRoadName();
        this.roadNumber = pothole.getRoadNumber();
        this.zipCode = pothole.getZipCode();
        this.roadAddress = pothole.getRoadAddress();
        this.thumbnail = pothole.getThumbnail();
        this.importance = pothole.getImportance();
        this.dangerous = pothole.getDangerous();
        this.progressStatus = pothole.getProcessStatus();
    }
}
