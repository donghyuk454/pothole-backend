package pothole_solution.core.domain.pothole.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
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
        JPAQuery<Pothole> potholeJPAQuery = jpaQueryFactory.selectFrom(pothole);
        if (potFltPotMngrServDto.getRoadCode().isEmpty()) {
            // 검색 조건 중 도로명 주소 포함되지 않은 경우
            potholeJPAQuery.where(
                    getImportanceFilter(potFltPotMngrServDto.getMinImportance(), potFltPotMngrServDto.getMaxImportance()),
                    getProgressFilter(potFltPotMngrServDto.getProcessStatus())
            );
        } else {
            // 검색 조건 중 도로명 주소 포함된 경우
            potholeJPAQuery.where(
                    getImportanceFilter(potFltPotMngrServDto.getMinImportance(), potFltPotMngrServDto.getMaxImportance()),
                    getProgressFilter(potFltPotMngrServDto.getProcessStatus()),
                    getRoadCodeFilter(potFltPotMngrServDto.getRoadCode())
            );
        }
        return potholeJPAQuery.fetch();
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

    private BooleanBuilder getRoadCodeFilter(List<String> roadCodes) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 도로명 입력
        if (roadCodes != null && !roadCodes.isEmpty()) {
            booleanBuilder.and(pothole.roadCode.in(roadCodes));
        }

        return booleanBuilder;
    }
}
