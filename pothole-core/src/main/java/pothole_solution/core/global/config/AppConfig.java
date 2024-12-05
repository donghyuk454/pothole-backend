package pothole_solution.core.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pothole_solution.core.global.util.formatter.LocalDateFormatter;

@Configuration
@EnableFeignClients(basePackages = {"pothole_solution.core.domain.pothole.service"})
public class AppConfig {
    @Bean
    public LocalDateFormatter localDateFormatter() {
        return new LocalDateFormatter();
    }

}
