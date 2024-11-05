package pothole_solution.manager.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.global.exception.CustomException;

@Getter
@AllArgsConstructor
public enum ReportType {
    COUNT("count", "기간별 개수 통계 조회 타입"),
    HISTORY("history", "기간별 포트홀 기록 조회 타입");

    private final String lower;
    private final String desc;

    public static ReportType enumOf(String type) {
        try {
            return ReportType.valueOf(type.toUpperCase());
        } catch (RuntimeException e) {
            throw CustomException.MISMATCH_REPORT_TYPE;
        }
    }
}
