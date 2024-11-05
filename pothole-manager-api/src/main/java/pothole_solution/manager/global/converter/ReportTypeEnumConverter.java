package pothole_solution.manager.global.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import pothole_solution.manager.report.entity.ReportType;

public class ReportTypeEnumConverter implements Converter<String, ReportType> {
    @Override
    public ReportType convert(@NotNull String type) {
        return ReportType.enumOf(type);
    }
}
