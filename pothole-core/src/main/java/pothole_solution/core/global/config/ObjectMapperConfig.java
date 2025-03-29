package pothole_solution.core.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.JacksonUtils;

@Configuration
public class ObjectMapperConfig {

    @Bean(name = "defaultObjectMapper")
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    @Primary
    @Bean(name = "enhancedObjectMapper")
    public ObjectMapper enhancedObjectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }
}
