package pothole_solution.manager.global.formatter;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pothole_solution.manager.global.converter.ReportCriteriaEnumConverter;
import pothole_solution.manager.global.converter.ReportPeriodEnumConverter;

@Configuration
public class EnumFormatter implements WebMvcConfigurer {
    @Override
    public void addFormatters(@NotNull FormatterRegistry registry) {
        registry.addConverter(new ReportPeriodEnumConverter());
        registry.addConverter(new ReportCriteriaEnumConverter());
    }
}
