package pothole_solution.detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan(basePackages = {"pothole_solution.core", "pothole_solution.detection"})
@EnableJpaRepositories(basePackages = {"pothole_solution.core", "pothole_solution.detection"})
@SpringBootApplication(scanBasePackages = {"pothole_solution.core", "pothole_solution.detection"})
public class KafkaConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class, args);
    }
}
