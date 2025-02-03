package pothole_solution.core.domain.pothole.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoadAddressSearchPolicy {
    FIRST(1, 10),
    SECOND(2, 20),
    THIRD(3, 30),
    FOURTH(4, 40),
    FIFTH(5, 50);

    RoadAddressSearchPolicy(int retry, int radius) {
        this.retry = retry;
        this.radius = radius;
    }

    private final int retry;
    private final int radius; // (radius)m 반경 기준 주소 검색
    private double[][] retryMatrix; // 재시도 Matrix 기준으로 재시도 수행

    private static final double BASE_LAT_RADIUS = 0.000009; // 위도 1m 기준
    private static final double BASE_LON_RADIUS = 0.000011; // 경도 1m 기준

    private static final double[][] BASE_MATRIX = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
    };

    private static final int MAX_RETRY = 5; // 최대 재시도 횟수

    public static RoadAddressSearchPolicy fromRetry(int retry) {
        return Arrays.stream(RoadAddressSearchPolicy.values())
                .filter(p -> p.getRetry() == retry)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 RoadAddress 검색 정책입니다. retry 값을 확인해주세요."));
    }

    public static int getMaxRetry() {
        return MAX_RETRY;
    }

    private static double[][] getBaseMatrixClone() {
        double[][] clone = new double[BASE_MATRIX.length][];
        for (int i = 0; i < BASE_MATRIX.length; i++) {
            clone[i] = BASE_MATRIX[i].clone();
        }
        return clone;
    }

    public double[][] getRetryMatrix() {
        if (this.retryMatrix != null) {
            return this.retryMatrix;
        }

        double[][] cloneMatrix = getBaseMatrixClone();

        Arrays.stream(cloneMatrix).forEach(vector -> {
            vector[0] *= BASE_LAT_RADIUS * this.radius;
            vector[1] *= BASE_LON_RADIUS * this.radius;
        });

        this.retryMatrix = cloneMatrix;

        return this.retryMatrix;
    }
}
