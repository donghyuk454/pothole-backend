package pothole_solution.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan("pothole_solution.core")
@EnableJpaRepositories({"pothole_solution.core", "pothole_solution.manager"})
@SpringBootApplication(scanBasePackages = {"pothole_solution.core", "pothole_solution.manager"})
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
