package pothole_solution.manager.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.global.exception.CustomException;

@Getter
@AllArgsConstructor
public enum ReportCriteria {
    DANGER("dangerous", "위험도"),
    IMPORT("importance", "중요도");

    private final String eng;
    private final String kor;

    public static ReportCriteria enumOf(String criteria) {
        try {
            return ReportCriteria.valueOf(criteria.toUpperCase());
        } catch (RuntimeException e) {
            throw CustomException.MISMATCH_PERIOD;
        }
    }
}
