package pothole_solution.core.domain.pothole.dto;

import lombok.Builder;
import lombok.Getter;
import pothole_solution.core.domain.pothole.entity.Progress;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PotFltPotMngrServDto {
    private Integer minImportance;
    private Integer maxImportance;
    private final Progress processStatus;
    private final String roadName;
    private List<String> roadCode = new ArrayList<>();

    @Builder
    public PotFltPotMngrServDto(Integer minImportance, Integer maxImportance, Progress processStatus,
                                String roadName) {
        this.minImportance = minImportance;
        this.maxImportance = maxImportance;
        this.processStatus = processStatus;
        this.roadName = roadName;
    }

    public void changeToAvailableImportance(Integer availableMinImportance, Integer availableMaxImportance) {
        this.minImportance = availableMinImportance;
        this.maxImportance = availableMaxImportance;
    }

    public void initRoadCode(List<String> roadCode) {
        this.roadCode = roadCode;
    }
}
