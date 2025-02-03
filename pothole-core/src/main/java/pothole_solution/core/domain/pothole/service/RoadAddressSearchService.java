package pothole_solution.core.domain.pothole.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pothole_solution.core.domain.pothole.entity.RoadAddress;
import pothole_solution.core.domain.pothole.entity.RoadAddressSearchApiResponse;

import static pothole_solution.core.global.exception.CustomException.INVALID_PARAMETER;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadAddressSearchService {
    private final RoadAddressSearchApi roadAddressSearchApi;

    public RoadAddress getRoadAddress(double lat, double lon){
        String point = getPoint(lat, lon);

        // 1차 조회
        RoadAddressSearchApiResponse response = getRoadAddressByApi(point);

        // 에러 발생 시 throw
        if (response.isInvalid()) {
            throw INVALID_PARAMETER;
        }

        // NOT_FOUND 일 경우 검색 좌표 보정 정책에 따라 retry
        if (response.isEmptyResult()) {
            return doRetry(lat, lon, 1);
        }

        return response.getFirstResult();
    }

    private RoadAddressSearchApiResponse getRoadAddressByApi(String point) {
        return roadAddressSearchApi.getRoadAddress(
                point,
                "9BFAD946-FBB2-3B7A-9179-82007C7B8D57",
                "address",
                "getAddress",
                "epsg:4326",
                "ROAD",
                true
        );
    }

    private String getPoint(double lat, double lon) {
        return lon + "," + lat;
    }

    public RoadAddress doRetry(double lat, double lon, int retry) {
        // 최대 수행 홧수 까지만 재시도
        log.info("재시도 횟수 : {}", retry);
        if (RoadAddressSearchPolicy.getMaxRetry() < retry) {
            return null;
        }

        // 좌표 보정 정책 matrix (재시도 당 총 8번)
        RoadAddressSearchPolicy policy = RoadAddressSearchPolicy.fromRetry(retry);
        log.info("policy radius : {}", policy.getRadius());
        double[][] retryMatrix = policy.getRetryMatrix();

        for (double[] retryVector: retryMatrix) {
            // 좌표 보정 후 재검색
            double retryLat = lat + retryVector[0];
            double retryLon = lon + retryVector[1];
            String retryPoint = getPoint(retryLat, retryLon);

            RoadAddressSearchApiResponse response = getRoadAddressByApi(retryPoint);

            // valid 할 경우 해당 결과 return
            if (response.isInvalid()) {
                continue;
            }

            // 성공 시 결과 return
            if (!response.isEmptyResult()) {
                log.info("좌표 찾음!! lat : {}, lon : {}, retry : {}, roadName : {}",
                        retryLat, retryLon, retry, response.getFirstResult().getStructure().getLevel4L());
                return response.getFirstResult();
            }
        }

        return doRetry(lat, lon, retry + 1);
    }
}