package pothole_solution.manager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class RespPotHistByPeriodDto {
    Long potholeId;
    Progress previousProgress;
    Progress latestProgress;
    int historyTotalCount;
    List<RespPotHistWithDateDto> respPotHistWithDateDtos;

    public RespPotHistByPeriodDto(Long potholeId) {
        this.potholeId = potholeId;
        respPotHistWithDateDtos = new ArrayList<>();
        historyTotalCount = 0;
    }

    public void setPreviousProgress(Progress previousProgress) {
        this.previousProgress = previousProgress;
    }

    public void setLatestProgress(Progress latestProgress) {
        this.latestProgress = latestProgress;
    }

    public void addTotalCount() {
        this.historyTotalCount += 1;
    }
}
