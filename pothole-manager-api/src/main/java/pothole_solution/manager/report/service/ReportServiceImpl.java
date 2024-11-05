package pothole_solution.manager.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pothole_solution.core.domain.pothole.entity.PotholeHistory;
import pothole_solution.core.domain.pothole.repository.PotholeHistoryRepository;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.dto.RespPotHistByPeriodDto;
import pothole_solution.manager.report.dto.RespPotHistWithDateDto;
import pothole_solution.manager.report.entity.ReportCriteria;
import pothole_solution.manager.report.entity.ReportPeriod;
import pothole_solution.manager.report.repository.ReportQueryDslRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pothole_solution.manager.report.service.AutoPeriodCalculator.isMonthly;
import static pothole_solution.manager.report.service.AutoPeriodCalculator.isWeekly;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportQueryDslRepository reportRepository;
    private final PotholeHistoryRepository potholeHistoryRepository;

    @Override
    public List<RespPotCriteriaCntByPeriodDto> getPeriodPotholeCriteriaCount(LocalDate startDate, LocalDate endDate,
                                                                             ReportPeriod reportPeriod, ReportCriteria criteria) {

        String queryOfPeriod = getQueryOfPeriod(startDate, endDate, reportPeriod);

        return reportRepository.getPotDngrCntByPeriod(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), queryOfPeriod, criteria);

    }

    private String getQueryOfPeriod(LocalDate startDate, LocalDate endDate, ReportPeriod reportPeriod) {
        if (!reportPeriod.equals(ReportPeriod.AUTO)) {
            return reportPeriod.getQueryOfPeriod();
        }
        if (isMonthly(startDate, endDate)) {
            return ReportPeriod.MONTHLY.getQueryOfPeriod();
        }
        if (isWeekly(startDate, endDate)) {
            return ReportPeriod.WEEKLY.getQueryOfPeriod();
        }
        return ReportPeriod.DAILY.getQueryOfPeriod();
    }

    @Override
    public List<RespPotHistByPeriodDto> getPeriodPotHist(LocalDateTime startDate, LocalDateTime endDate) {
        Map<Long, RespPotHistByPeriodDto> mapOfDtos = new HashMap<>();

        // 기간 내 변화가 있는 포트홀 히스토리 목록 추출
        List<PotholeHistory> allHistoryInPeriod = potholeHistoryRepository.findAllInPeriodOrderByCreatedAtDesc(startDate, endDate);
        List<Long> potholeIds = allHistoryInPeriod.stream()
                .map(potholeHistory -> potholeHistory.getPothole().getPotholeId())
                .distinct() // 중복 제거
                .toList();

        // 포트홀 별 Dto를 값으로 가지는 맵 생성
        potholeIds.forEach(potholeId -> {
            mapOfDtos.put(potholeId, new RespPotHistByPeriodDto(potholeId));
        });

        // 포트홀 별 기간 전 히스토리 상태 가져와서 매핑
        List<PotholeHistory> previousHistories = potholeHistoryRepository.findPreviousDate(potholeIds, startDate);
        previousHistories.forEach(previousHistory -> {
            Long potholeId = previousHistory.getPothole().getPotholeId();
            RespPotHistByPeriodDto respPotHistByPeriodDto = mapOfDtos.get(potholeId);
            // 이전 상태가 없다면 값 채워줌. 있다면 pass
            if (respPotHistByPeriodDto.getPreviousProgress() == null) {
                respPotHistByPeriodDto.setPreviousProgress(previousHistory.getProcessStatus());
            }
        });

        // 포트홀 별 기간동안의 히스토리 상태 가져와서 매핑
        allHistoryInPeriod.forEach(potholeHistory -> {
            Long potholeId = potholeHistory.getPothole().getPotholeId();
            RespPotHistByPeriodDto respPotHistByPeriodDto = mapOfDtos.get(potholeId);
            // 최신 상태가 없다면 값 채워줌. 있다면 pass
            if (respPotHistByPeriodDto.getLatestProgress() == null) {
                respPotHistByPeriodDto.setLatestProgress(potholeHistory.getProcessStatus());
            }

            // 히스토리N
            respPotHistByPeriodDto.getRespPotHistWithDateDtos().add(
                    new RespPotHistWithDateDto(
                            potholeHistory.getPotholeHistoryId(),
                            potholeHistory.getProcessStatus(),
                            potholeHistory.getCreatedAt()
                    )
            );

            // 히스토리 개수 증가
            respPotHistByPeriodDto.addTotalCount();
        });

        return new ArrayList<>(mapOfDtos.values());
    }

}
