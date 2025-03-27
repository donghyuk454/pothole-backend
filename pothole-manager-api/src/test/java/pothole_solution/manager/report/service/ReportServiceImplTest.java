package pothole_solution.manager.report.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import pothole_solution.core.global.config.QueryDslConfig;
import pothole_solution.core.global.util.alarm.slack.SlackService;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;
import pothole_solution.manager.report.entity.ReportPeriod;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
@MockBean(SlackService.class)
class ReportServiceImplTest {

    @Autowired
    private ReportService reportService;

    @Test
    void getPeriodPotholeCriteriaCount_Monthly() {
        //when
        List<RespPotCriteriaCntByPeriodDto> counts = reportService.getPeriodPotholeCriteriaCount(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 10),
                ReportPeriod.MONTHLY,
                ReportCriteria.DANGER);

        //then
        assertThat(counts).hasSize(3);
    }

    @Test
    void getPeriodPotholeCriteriaCount_Weekly() {
        //when
        List<RespPotCriteriaCntByPeriodDto> counts = reportService.getPeriodPotholeCriteriaCount(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 10),
                ReportPeriod.WEEKLY,
                ReportCriteria.DANGER);

        //then
        assertThat(counts).hasSize(10);
    }

    @Test
    void getPeriodPotholeCriteriaCount_Daily() {
        //when
        List<RespPotCriteriaCntByPeriodDto> counts = reportService.getPeriodPotholeCriteriaCount(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 10),
                ReportPeriod.DAILY,
                ReportCriteria.DANGER);

        //then
        assertThat(counts).hasSize(69);
    }

    @Test
    void getPeriodPotholeCriteriaCount_Auto() {
        //when
        List<RespPotCriteriaCntByPeriodDto> counts = reportService.getPeriodPotholeCriteriaCount(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 10),
                ReportPeriod.AUTO,
                ReportCriteria.DANGER);

        //then
        assertThat(counts).hasSize(10);
    }
}