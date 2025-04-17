package pothole_solution.core.domain.pothole.service;

import java.util.List;

public interface RoadAddressInfoService {
    List<String> getRoadCodeByRoadName(String roadName);
}
