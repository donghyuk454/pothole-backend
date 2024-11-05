package pothole_solution.manager.report.service;

import pothole_solution.manager.report.dto.RespPotHistByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;
import pothole_solution.manager.report.entity.ReportPeriod;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    List<RespPotCriteriaCntByPeriodDto> getPeriodPotholeCriteriaCount(LocalDate startDate, LocalDate endDate,
                                                                      ReportPeriod period, ReportCriteria criteria);
    List<RespPotHistByPeriodDto> getPeriodPotHist(LocalDateTime startDate, LocalDateTime endDate);
}
