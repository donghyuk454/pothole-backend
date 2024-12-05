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
}
