package pothole_solution.core.domain.pothole.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pothole_solution.core.domain.pothole.entity.RoadAddress;
import pothole_solution.core.domain.pothole.entity.RoadAddressSearchApiResponse;

import static pothole_solution.core.global.exception.CustomException.INVALID_PARAMETER;

@Service
@RequiredArgsConstructor
public class RoadAddressSearchService {
    private final RoadAddressSearchApi roadAddressSearchApi;

    public RoadAddress getRoadAddress(String point){
        RoadAddressSearchApiResponse response = roadAddressSearchApi.getRoadAddress(
                point,
                "9BFAD946-FBB2-3B7A-9179-82007C7B8D57",
                "address",
                "getAddress",
                "epsg:4326",
                "ROAD",
                true
        );

        if (response.getResponse().getStatus().equals("ERROR")) {
            throw INVALID_PARAMETER;
        }

        return response.getResponse().getResult().get(0);
    }

}