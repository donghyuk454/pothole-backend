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
import pothole_solution.manager.report.repository.ReportRepositoryImpl;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepositoryImpl reportRepository;
    private final PotholeHistoryRepository potholeHistoryRepository;

    @Override
    public List<RespPotCriteriaCntByPeriodDto> getPeriodPotholeCriteriaCount(LocalDate startDate, LocalDate endDate,
                                                                             ReportPeriod reportPeriod, ReportCriteria criteria) {

        String queryOfPeriod = reportPeriod.getQueryOfPeriodWithDate(startDate, endDate);

        List<RespPotCriteriaCntByPeriodDto> defaultList = getDefaultCriteriaCounts(startDate, endDate, queryOfPeriod);

        List<RespPotCriteriaCntByPeriodDto> reportResult = reportRepository.getPotDngrCntByPeriod(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), queryOfPeriod, criteria);

        return mergeList(defaultList, reportResult);
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

    private static List<RespPotCriteriaCntByPeriodDto> getDefaultCriteriaCounts(LocalDate startDate, LocalDate endDate, String queryPeriod) {
        String monthly = ReportPeriod.MONTHLY.getQueryOfPeriod();
        String weekly = ReportPeriod.WEEKLY.getQueryOfPeriod();

        if (monthly.equals(queryPeriod)) {
            return generateMonthlyCounts(startDate, endDate);
        } else if (weekly.equals(queryPeriod)) {
            return generateWeeklyCounts(startDate, endDate);
        }

        return generateDailyCounts(startDate, endDate);
    }

    private static RespPotCriteriaCntByPeriodDto createZeroCriteriaCntDto(String period) {
        return new RespPotCriteriaCntByPeriodDto(period, 0L, 0L,0L,0L, 0L);
    }

    private static List<RespPotCriteriaCntByPeriodDto> generateMonthlyCounts(LocalDate startDate, LocalDate endDate) {
        List<RespPotCriteriaCntByPeriodDto> result = new ArrayList<>();

        LocalDate monthCursor = startDate.withDayOfMonth(1);
        while (!monthCursor.isAfter(endDate)) {
            result.add(createZeroCriteriaCntDto(monthCursor.format(DateTimeFormatter.ofPattern("yyyy-MM"))));
            monthCursor = monthCursor.plusMonths(1);
        }

        return result;
    }

    private static List<RespPotCriteriaCntByPeriodDto> generateWeeklyCounts(LocalDate startDate, LocalDate endDate) {
        List<RespPotCriteriaCntByPeriodDto> result = new ArrayList<>();

        LocalDate weekCursor = startDate;
        while (!weekCursor.isAfter(endDate)) {
            YearMonth currentMonth = YearMonth.from(weekCursor);
            if (currentMonth.equals(YearMonth.from(weekCursor.with(DayOfWeek.MONDAY)))) {
                String yearMonth = currentMonth.toString();
                int weekOfMonth = (weekCursor.getDayOfMonth() - 1) / 7 + 1;
                result.add(createZeroCriteriaCntDto(yearMonth + "-" + weekOfMonth));
            }
            weekCursor = weekCursor.plusWeeks(1);
        }

        return result;
    }

    private static List<RespPotCriteriaCntByPeriodDto> generateDailyCounts(LocalDate startDate, LocalDate endDate) {
        List<RespPotCriteriaCntByPeriodDto> result = new ArrayList<>();

        LocalDate dayCursor = startDate;
        while (!dayCursor.isAfter(endDate)) {
            result.add(createZeroCriteriaCntDto(dayCursor.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            dayCursor = dayCursor.plusDays(1);
        }

        return result;
    }

    private List<RespPotCriteriaCntByPeriodDto> mergeList(List<RespPotCriteriaCntByPeriodDto> baseList, List<RespPotCriteriaCntByPeriodDto> newList) {
        Map<String, RespPotCriteriaCntByPeriodDto> mergedMap = new HashMap<>();

        baseList.forEach(item -> mergedMap.put(item.getPeriod(), item));
        newList.forEach(item -> mergedMap.put(item.getPeriod(), item));

        return mergedMap.values().stream()
                .sorted(Comparator.comparing(RespPotCriteriaCntByPeriodDto::getPeriod))
                .toList();
    }
}
