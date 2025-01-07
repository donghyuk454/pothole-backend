package pothole_solution.core.global.exception.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pothole_solution.core.global.exception.kafka.entity.FailedKafkaMessage;

public interface FailedKafkaMessageRepository extends JpaRepository<FailedKafkaMessage, Long> {
}
