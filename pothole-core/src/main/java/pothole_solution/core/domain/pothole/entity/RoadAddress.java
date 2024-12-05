package pothole_solution.core.domain.pothole.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoadAddress {
    private String zipcode;
    private String text;
    private Structure structure;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Structure {
        private String level0;
        private String level1;
        private String level2;
        private String level3;
        private String level4L;
        private String level4LC;
        private String level4A;
        private String level4AC;
        private String level5;
        private String detail;
    }
}
