package pothole_solution.core.domain.pothole.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pothole_solution.core.domain.pothole.repository.RoadAddressInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadAddressInfoServiceImpl implements RoadAddressInfoService {
    private final RoadAddressInfoRepository roadAddressInfoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<String> getRoadCodeByRoadName(String roadName) {
        // TODO: Pothole 컬럼명 및 zipcode 컬럼 삭제하면 끝남!
        return roadAddressInfoRepository.findRoadCodesByRoadName(roadName);
    }
}
