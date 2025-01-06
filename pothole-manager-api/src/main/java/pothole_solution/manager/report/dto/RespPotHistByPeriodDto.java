package pothole_solution.manager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class RespPotHistByPeriodDto implements RespPotReportDto {
    Long potholeId;
    Progress previousProgress;
    Progress latestProgress;
    int historyTotalCount;
    List<RespPotHistWithDateDto> potholeHistories;

    public RespPotHistByPeriodDto(Long potholeId) {
        this.potholeId = potholeId;
        potholeHistories = new ArrayList<>();
        historyTotalCount = 0;
    }

    public void setPreviousProgressIfNull(Progress previousProgress) {
        if (this.previousProgress == null)
            this.previousProgress = previousProgress;
    }

    public void setLatestProgressIfNull(Progress latestProgress) {
        if (this.latestProgress == null)
            this.latestProgress = latestProgress;
    }

    public void addTotalCount() {
        this.historyTotalCount += 1;
    }
}
