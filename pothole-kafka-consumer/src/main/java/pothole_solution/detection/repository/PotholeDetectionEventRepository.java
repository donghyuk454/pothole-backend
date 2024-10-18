package pothole_solution.detection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pothole_solution.detection.entity.PotholeDetectionEvent;

@Repository
public interface PotholeDetectionEventRepository extends JpaRepository<PotholeDetectionEvent, Long> {
}
