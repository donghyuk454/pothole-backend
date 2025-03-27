package pothole_solution.manager.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pothole_solution.core.domain.pothole.entity.Pothole;

@Repository
public interface ReportRepository extends JpaRepository<Pothole, Long>, ReportRepositoryCustom {
}
