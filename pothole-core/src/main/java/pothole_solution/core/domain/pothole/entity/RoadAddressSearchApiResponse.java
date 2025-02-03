package pothole_solution.core.domain.pothole.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoadAddressSearchApiResponse {
    private Response response;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Service service;
        private String status;
        private List<RoadAddress> result;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Service {
            private String name;
            private String version;
            private String operation;
            private String time;
        }
    }

    // 결과가 유효한 지 확인
    public boolean isInvalid() {
        return response == null ||
                response.getStatus() == null ||
                response.getStatus().equals("ERROR");
    }

    // 주소 값이 비어 있는 지 확인
    public boolean isEmptyResult() {
        return response.getStatus().equals("NOT_FOUND") ||
                response.getResult() == null ||
                response.getResult().size() == 0;
    }

    public RoadAddress getFirstResult() {
        return response.getResult().get(0);
    }
}
