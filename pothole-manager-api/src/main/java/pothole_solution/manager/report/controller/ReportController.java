package pothole_solution.manager.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pothole_solution.core.global.util.response.BaseResponse;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.dto.RespPotHistByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;
import pothole_solution.manager.report.entity.ReportPeriod;
import pothole_solution.manager.report.entity.ReportType;
import pothole_solution.manager.report.service.ReportService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pothole/v1/manager")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/pothole-report")
    public BaseResponse<List<?>> getPotDngrCntByPeriod(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                   @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                                   @RequestParam(value = "reportType") ReportType reportType,
                                                                                   @RequestParam(value = "reportPeriod", required = false) ReportPeriod reportPeriod,
                                                                                   @RequestParam(value = "criteria", required = false) ReportCriteria criteria) {

        if (reportType == ReportType.COUNT) {
            List<RespPotCriteriaCntByPeriodDto> periodPotholeCounts = reportService.getPeriodPotholeCriteriaCount(startDate, endDate, reportPeriod, criteria);

            return new BaseResponse<>(periodPotholeCounts);
        } else{
            List<RespPotHistByPeriodDto> periodPotHists = reportService.getPeriodPotHist(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

            return new BaseResponse<>(periodPotHists);
        }
    }

    @GetMapping("/pothole-report/history")
    public BaseResponse<List<RespPotHistByPeriodDto>> getPeriodPotHist(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                       @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<RespPotHistByPeriodDto> periodPotHists = reportService.getPeriodPotHist(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        return new BaseResponse<>(periodPotHists);
    }
}
