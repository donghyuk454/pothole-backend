package pothole_solution.manager.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pothole_solution.manager.report.dto.RespPotCriteriaCntByPeriodDto;
import pothole_solution.manager.report.entity.ReportCriteria;

import java.time.LocalDateTime;
import java.util.List;

import static pothole_solution.core.domain.pothole.entity.QPothole.pothole;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReportQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<RespPotCriteriaCntByPeriodDto> getPotDngrCntByPeriod(LocalDateTime startDate, LocalDateTime endDate,
                                                                     String queryOfPeriod, ReportCriteria criteria) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(RespPotCriteriaCntByPeriodDto.class,
                                convertDateFormat(queryOfPeriod),
                                countCriteriaBetween(criteria, 1, 20),
                                countCriteriaBetween(criteria, 21, 40),
                                countCriteriaBetween(criteria, 41, 60),
                                countCriteriaBetween(criteria, 61, 80),
                                countCriteriaBetween(criteria, 81, 100)
                        )
                )
                .from(pothole)
                .where(pothole.createdAt.between(startDate, endDate))
                .groupBy(convertDateFormat(queryOfPeriod))
                .orderBy(convertDateFormat(queryOfPeriod).asc())
                .fetch();
    }

    private static StringTemplate convertDateFormat(String queryOfPeriod) {
        return Expressions.stringTemplate(
                    "to_char({0},'" + queryOfPeriod + "')"
                    , pothole.createdAt);
    }

    // TODO: 리펙토링 필요
    private static NumberExpression<Long> countCriteriaBetween(ReportCriteria criteria, int from, int to) {
        if (criteria == ReportCriteria.DANGER) {
            return Expressions.cases()
                    .when(pothole.dangerous.between(from, to))
                    .then(1L)
                    .otherwise(0L)
                    .sum();
        } else {
            return Expressions.cases()
                    .when(pothole.importance.between(from, to))
                    .then(1L)
                    .otherwise(0L)
                    .sum();
        }
    }

}
