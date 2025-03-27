package pothole_solution.manager.report.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;
import pothole_solution.core.domain.pothole.repository.PotholeRepository;
import pothole_solution.core.global.config.QueryDslConfig;
import pothole_solution.core.global.util.alarm.slack.SlackService;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;
import pothole_solution.manager.report.entity.ReportPeriod;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
@MockBean(SlackService.class)
class ReportQueryDslRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PotholeRepository potholeRepository;

    @Autowired
    private EntityManager em;

    GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        // GeometryFactory 객체 생성
        geometryFactory = new GeometryFactory();

        // x=10.5, y=20.5인 Point 객체 생성
        Point point = geometryFactory.createPoint(new Coordinate(10.5, 20.5));
        Pothole pothole = Pothole.builder()
                .processStatus(Progress.REGISTER)
                .point(point)
                .dangerous(10)
                .importance(10)
                .build();
        potholeRepository.save(pothole);

        em.flush();
        em.clear();

    }

    @Test
    @DisplayName("Danger 월 검색")
    void getPotDngrCntByPeriod_Danger_Month() {
        //given
        createPothole(10.5, 20.5, 10, 10);
        createPothole(10.5, 20.5, 30, 30);
        createPothole(10.5, 20.5, 50, 50);

        // when
        List<RespPotCriteriaCntByPeriodDto> result =
                reportRepository.getPotDngrCntByPeriod(LocalDateTime.of(2025, 1,1, 0, 0, 0),
                        LocalDateTime.now(),
                        ReportPeriod.MONTHLY.getQueryOfPeriod(),
                        ReportCriteria.DANGER);

        result.forEach(r -> System.out.println(r.toString()));

        //then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Danger 주 검색")
    void getPotDngrCntByPeriod_Danger_Week() {
        //given
        createPothole(10.5, 20.5, 10, 10);
        createPothole(10.5, 20.5, 30, 30);
        createPothole(10.5, 20.5, 50, 50);

        // when
        List<RespPotCriteriaCntByPeriodDto> result =
                reportRepository.getPotDngrCntByPeriod(LocalDateTime.of(2025, 1,1, 0, 0, 0),
                        LocalDateTime.now(),
                        ReportPeriod.WEEKLY.getQueryOfPeriod(),
                        ReportCriteria.DANGER);

        result.forEach(r -> System.out.println(r.toString()));

        //then
        assertThat(result).hasSize(3);
    }
    @Test
    @DisplayName("Danger 일 검색")
    void getPotDngrCntByPeriod_Danger_Day() {
        //given
        createPothole(10.5, 20.5, 10, 10);
        createPothole(10.5, 20.5, 30, 30);
        createPothole(10.5, 20.5, 50, 50);

        // when
        List<RespPotCriteriaCntByPeriodDto> result =
                reportRepository.getPotDngrCntByPeriod(LocalDateTime.of(2025, 1,1, 0, 0, 0),
                        LocalDateTime.now(),
                        ReportPeriod.DAILY.getQueryOfPeriod(),
                        ReportCriteria.DANGER);

        result.forEach(r -> System.out.println(r.toString()));

        //then
        assertThat(result).hasSize(3);
    }


    private void createPothole(double x, double y, int danger, int importance) {
        Point point = geometryFactory.createPoint(new Coordinate(x, y));
        Pothole pothole = Pothole.builder()
                .processStatus(Progress.REGISTER)
                .point(point)
                .dangerous(danger)
                .importance(importance)
                .build();
        potholeRepository.save(pothole);
    }
}