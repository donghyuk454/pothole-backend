package pothole_solution.manager.global.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import pothole_solution.manager.report.entity.ReportCriteria;

public class ReportCriteriaEnumConverter implements Converter<String, ReportCriteria> {
    @Override
    public ReportCriteria convert(@NotNull String criteria) {
        return ReportCriteria.enumOf(criteria);
    }
}
