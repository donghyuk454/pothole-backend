package pothole_solution.manager.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        String queryOfPeriod = reportPeriod.getQueryOfPeriodWithDate(startDate, endDate);

        return reportRepository.getPotDngrCntByPeriod(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), queryOfPeriod, criteria);
    }

    @Override
    public List<RespPotHistByPeriodDto> getPeriodPotHist(LocalDateTime startDate, LocalDateTime endDate) {
        // 기간 내 변화가 있는 포트홀 히스토리 목록 추출
        // 포트홀 별 Dto를 값으로 가지는 맵 생성
        List<PotholeHistory> allHistoryInPeriod = potholeHistoryRepository.findAllInPeriodOrderByCreatedAtDesc(startDate, endDate);

        Map<Long, RespPotHistByPeriodDto> responseDict = allHistoryInPeriod.stream()
                .map(ReportServiceImpl::getPotholeIdByHistory)
                .distinct()
                .collect(Collectors.toMap(potholeId -> potholeId, RespPotHistByPeriodDto::new));

        List<Long> potholeIds = responseDict.keySet().stream().toList();

        // 포트홀 별 기간 전 히스토리 상태 가져와서 매핑
        List<PotholeHistory> previousHistories = potholeHistoryRepository.findPreviousDate(potholeIds, startDate);
        previousHistories.forEach(previousHistory -> {
            RespPotHistByPeriodDto respPotHistByPeriodDto = responseDict.get( getPotholeIdByHistory(previousHistory) );

            // 이전 상태가 없다면 값 채워줌. 있다면 pass
            respPotHistByPeriodDto.setPreviousProgressIfNull(previousHistory.getProcessStatus());
        });

        // 포트홀 별 기간동안의 히스토리 상태 가져와서 매핑
        allHistoryInPeriod.forEach(potholeHistory -> {
            RespPotHistByPeriodDto respPotHistByPeriodDto = responseDict.get( getPotholeIdByHistory(potholeHistory) );

            // 최신 상태가 없다면 값 채워줌. 있다면 pass
            respPotHistByPeriodDto.setLatestProgressIfNull(potholeHistory.getProcessStatus());

            // 히스토리 추가
            respPotHistByPeriodDto.getPotholeHistories().add( createPotholeHistoryDto(potholeHistory) );

            // 히스토리 개수 증가
            respPotHistByPeriodDto.addTotalCount();
        });

        return new ArrayList<>(responseDict.values());
    }


    private static Long getPotholeIdByHistory(PotholeHistory previousHistory) {
        return previousHistory.getPothole().getPotholeId();
    }

    @NotNull
    private static RespPotHistWithDateDto createPotholeHistoryDto(PotholeHistory potholeHistory) {
        return new RespPotHistWithDateDto(
                potholeHistory.getPotholeHistoryId(),
                potholeHistory.getProcessStatus(),
                potholeHistory.getCreatedAt()
        );
    }

}
