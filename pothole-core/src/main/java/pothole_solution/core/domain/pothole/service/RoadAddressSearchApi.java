package pothole_solution.core.domain.pothole.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pothole_solution.core.domain.pothole.entity.RoadAddressSearchApiResponse;

@FeignClient(value= "search", url = "https://api.vworld.kr")
public interface RoadAddressSearchApi {

    @GetMapping(value = "/req/address",produces = MediaType.APPLICATION_JSON_VALUE)
    RoadAddressSearchApiResponse getRoadAddress(@RequestParam("point") String point,
                                                @RequestParam("key") String key,
                                                @RequestParam("service") String service,
                                                @RequestParam("request") String request,
                                                @RequestParam("crs") String crs,
                                                @RequestParam("type") String type,
                                                @RequestParam("simple") boolean simple
    );

}