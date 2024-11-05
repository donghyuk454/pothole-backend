package pothole_solution.core.domain.pothole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pothole_solution.core.domain.pothole.entity.PotholeHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface PotholeHistoryRepository extends JpaRepository<PotholeHistory, Long> {
    @Query("select ph from PotholeHistory ph " +
            "join fetch ph.potholeHistoryImages " +
            "where ph.pothole.potholeId = :potholeId")
    List<PotholeHistory> findAllByPotholeId(@Param("potholeId") Long potholeId);

    @Modifying
    @Query("delete from PotholeHistory ph " +
            "where ph.pothole.potholeId = :potholeId")
    void deleteAllByPotholeId(@Param("potholeId") Long potholeId);

    @Query("select ph from PotholeHistory ph " +
            "join fetch ph.pothole p " +
            "where ph.createdAt between :startDate and :endDate " +
            "order by ph.createdAt desc")
    List<PotholeHistory> findAllInPeriodOrderByCreatedAtDesc(@Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);

    @Query("select ph from PotholeHistory ph " +
            "join fetch ph.pothole p " +
            "where p.potholeId in :potholeIds " +
                "and ph.createdAt < :startDate " +
            "order by ph.createdAt desc")
    List<PotholeHistory> findPreviousDate(@Param("potholeIds") List<Long> potholeIds,
                                          @Param("startDate") LocalDateTime startDate);
}
