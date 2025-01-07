package pothole_solution.core.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "defaultRestTemplate")
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }
}
