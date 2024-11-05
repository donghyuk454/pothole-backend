package pothole_solution.manager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RespPotHistWithDateDto {
    Long potholeHistoryId;
    Progress progress;
    LocalDateTime createdAt;
}
