package pothole_solution.core.domain.pothole.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class RoadAddressInfo {
    @EmbeddedId
    private RoadAddressInfoId roadAddressInfoId;
    private String road_name;
    private String road_name_eng;
    private String ctdo_name;
    private String ccw_name;
    private String town_type;
    private String town_code;
    private String town_name;
    private String upp_road_code;
    private String upp_road_name;
    private String abolition_type;
    private String change_reason;
    private String change_info;
    private String ctdo_name_eng;
    private String ccw_name_eng;
    private String town_name_eng;
    private String notify_date;
    private String expire_date;

    @Builder
    public RoadAddressInfo(RoadAddressInfoId roadAddressInfoId, String road_name, String road_name_eng,
                           String ctdo_name, String ccw_name, String town_type,
                           String town_code, String town_name, String upp_road_code,
                           String upp_road_name, String abolition_type, String change_reason,
                           String change_info, String ctdo_name_eng, String ccw_name_eng,
                           String town_name_eng, String notify_date, String expire_date
    ) {
        this.roadAddressInfoId = roadAddressInfoId;
        this.road_name = road_name;
        this.road_name_eng = road_name_eng;
        this.ctdo_name = ctdo_name;
        this.ccw_name = ccw_name;
        this.town_type = town_type;
        this.town_code = town_code;
        this.town_name = town_name;
        this.upp_road_code = upp_road_code;
        this.upp_road_name = upp_road_name;
        this.abolition_type = abolition_type;
        this.change_reason = change_reason;
        this.change_info = change_info;
        this.ctdo_name_eng = ctdo_name_eng;
        this.ccw_name_eng = ccw_name_eng;
        this.town_name_eng = town_name_eng;
        this.notify_date = notify_date;
        this.expire_date = expire_date;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class RoadAddressInfoId implements Serializable {
        private String ccw_code;
        private String road_code;
        private String town_serial_number;

        @Builder
        public RoadAddressInfoId(String ccw_code, String road_code, String town_serial_number) {
            this.ccw_code = ccw_code;
            this.road_code = road_code;
            this.town_serial_number = town_serial_number;
        }
    }
}
