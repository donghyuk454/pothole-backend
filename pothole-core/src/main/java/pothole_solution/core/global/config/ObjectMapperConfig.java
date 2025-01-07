package pothole_solution.core.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean(name = "defaultObjectMapper")
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }
}
