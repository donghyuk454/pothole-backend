package pothole_solution.manager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RespPotCriteriaCntByPeriodDto {
    String period;
    Long criteria0to20;
    Long criteria20to40;
    Long criteria40to60;
    Long criteria60to80;
    Long criteria80to100;
}
