package pothole_solution.manager.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
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
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RespPotCriteriaCntByPeriodDto> getPotDngrCntByPeriod(LocalDateTime startDate,
                                                                     LocalDateTime endDate,
                                                                     String queryOfPeriod,
                                                                     ReportCriteria criteria) {

        NumberPath<Integer> searchCriteria = ReportCriteria.DANGER.equals(criteria) ? pothole.dangerous : pothole.importance;

        StringTemplate periodTemplate = convertDateFormat(queryOfPeriod);

        return jpaQueryFactory
                .select(
                        Projections.constructor(RespPotCriteriaCntByPeriodDto.class,
                                periodTemplate,
                                countCriteriaBetween(searchCriteria, 1, 20),
                                countCriteriaBetween(searchCriteria, 21, 40),
                                countCriteriaBetween(searchCriteria, 41, 60),
                                countCriteriaBetween(searchCriteria, 61, 80),
                                countCriteriaBetween(searchCriteria, 81, 100)
                        )
                )
                .from(pothole)
                .where(pothole.createdAt.between(startDate, endDate))
                .groupBy(periodTemplate)
                .orderBy(convertDateFormat(queryOfPeriod).asc())
                .fetch();
    }

    private static StringTemplate convertDateFormat(String queryOfPeriod) {
        return Expressions.stringTemplate(
                    "to_char({0},'" + queryOfPeriod + "')"
                    , pothole.createdAt);
    }

    private static NumberExpression<Long> countCriteriaBetween(NumberPath<Integer> criteria, int from, int to) {
        return criteria.between(from, to).count();
    }
}
