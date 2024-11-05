package pothole_solution.core.domain.pothole.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pothole_solution.core.domain.pothole.dto.PotFltPotMngrServDto;
import pothole_solution.core.domain.pothole.entity.Pothole;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.List;

import static pothole_solution.core.domain.pothole.entity.QPothole.pothole;

@Repository
@RequiredArgsConstructor
public class PotholeQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Pothole> findByFilter(PotFltPotMngrServDto potFltPotMngrServDto) {
        return jpaQueryFactory.selectFrom(pothole)
                .where(
                        getImportanceFilter(potFltPotMngrServDto.getMinImportance(), potFltPotMngrServDto.getMaxImportance()),
                        getProgressFilter(potFltPotMngrServDto.getProcessStatus()),
                        getRoadNameFilter(potFltPotMngrServDto.getRoadName())
                )
                .fetch();
    }

    private BooleanBuilder getProgressFilter(Progress processStatus) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 진행 상태 입력
        if (processStatus != null) {
            booleanBuilder.and(pothole.processStatus.eq(processStatus));
        }

        return booleanBuilder;
    }

    private BooleanBuilder getImportanceFilter(Integer minImportance, Integer maxImportance) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 최소, 최대 모두 입력
        if (minImportance != 0 && maxImportance != 100) {
            booleanBuilder.and(pothole.importance.between(minImportance, maxImportance));
        }

        // 최소만 입력
        if (minImportance != 0 && maxImportance == 100) {
            booleanBuilder.and(pothole.importance.goe(minImportance));
        }

        // 최대만 입력
        if (minImportance == 0 && maxImportance != 100) {
            booleanBuilder.and(pothole.importance.loe(maxImportance));
        }

        return booleanBuilder;
    }

    private BooleanBuilder getRoadNameFilter(String roadName) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 도로명 입력
        if (roadName != null && !roadName.isBlank()) {
            booleanBuilder.and(pothole.roadName.eq(roadName));
        }

        return booleanBuilder;
    }
}
