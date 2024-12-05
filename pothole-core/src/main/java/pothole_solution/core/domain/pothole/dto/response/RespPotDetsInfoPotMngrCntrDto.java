package pothole_solution.core.domain.pothole.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RespPotDetsInfoPotMngrCntrDto {
    private Long potholeId;
    private String roadAddress;
    private double lat;
    private double lon;
    private String thumbnail;
    private Integer importance;
    private Progress progressStatus;
    private Integer dangerous;

    @JsonProperty(value = "potholeHistories")
    private List<RespPotHistGetPotMngrCntrDto> potholeHistoryResponseDto = new ArrayList<>();

    @Builder
    public RespPotDetsInfoPotMngrCntrDto(Pothole pothole, List<RespPotHistGetPotMngrCntrDto> respPotHistGetPotMngrCntrDtoList) {
        this.potholeId = pothole.getPotholeId();
        this.roadAddress = pothole.getRoadAddress();
        this.lat = pothole.getPoint().getY();
        this.lon = pothole.getPoint().getX();
        this.thumbnail = pothole.getThumbnail();
        this.importance = pothole.getImportance();
        this.progressStatus = pothole.getProcessStatus();
        this.dangerous = pothole.getDangerous();
        this.potholeHistoryResponseDto = respPotHistGetPotMngrCntrDtoList;
    }
}
