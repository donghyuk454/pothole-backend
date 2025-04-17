package pothole_solution.core.domain.pothole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pothole_solution.core.domain.pothole.entity.RoadAddressInfo;

import java.util.List;
import java.util.Optional;

public interface RoadAddressInfoRepository extends JpaRepository<RoadAddressInfo, RoadAddressInfo.RoadAddressInfoId>  {
    @Query("select distinct rai.roadAddressInfoId.road_code " +
            "from RoadAddressInfo rai " +
            "where rai.road_name like concat(:roadName, '%')")
    List<String> findRoadCodesByRoadName(@Param("roadName") String roadName);
}
