package pothole_solution.manager.report.repository;

import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepositoryCustom {

    List<RespPotCriteriaCntByPeriodDto> getPotDngrCntByPeriod(LocalDateTime startDate,
                                                              LocalDateTime endDate,
                                                              String queryOfPeriod,
                                                              ReportCriteria criteria);
}
