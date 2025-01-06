package pothole_solution.manager.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pothole_solution.core.global.exception.CustomException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public enum ReportPeriod {
    MONTHLY("YYYY-MM"),
    WEEKLY("YYYY-MM-W"),
    DAILY("YYYY-MM-DD"),
    AUTO("auto");

    private final String queryOfPeriod;

    private static final int CRITERIA_OF_MONTHLY = 3;
    private static final int CRITERIA_OF_WEEKLY = 3;

    public static ReportPeriod enumOf(String period) {
        try {
            return ReportPeriod.valueOf(period.toUpperCase());
        } catch (RuntimeException e) {
            throw CustomException.MISMATCH_PERIOD;
        }
    }

    public String getQueryOfPeriodWithDate(LocalDate startDate, LocalDate endDate) {
        if (isAuto()) {
            return this.getQueryOfPeriod();
        }

        return getAutoQueryOfPeriod(startDate, endDate);
    }

    private boolean isAuto() {
        return ReportPeriod.AUTO.equals(this);
    }

    private static String getAutoQueryOfPeriod(LocalDate startDate, LocalDate endDate) {
        if (ChronoUnit.MONTHS.between(startDate, endDate) >= CRITERIA_OF_MONTHLY) {
            return ReportPeriod.MONTHLY.getQueryOfPeriod();
        }
        if (ChronoUnit.WEEKS.between(startDate, endDate) >= CRITERIA_OF_WEEKLY) {
            return ReportPeriod.WEEKLY.getQueryOfPeriod();
        }
        return ReportPeriod.DAILY.getQueryOfPeriod();
    }
}
