package pothole_solution.core.domain.pothole.dto.message.creation;

import lombok.NoArgsConstructor;
import lombok.ToString;
import pothole_solution.core.domain.pothole.dto.message.BasicMessage;

@ToString
@NoArgsConstructor
public class PotholeCreationMessage extends BasicMessage<PotholeCreationContent> {
    public PotholeCreationMessage(String id, PotholeCreationContent content) {
        super(id, content);
    }
}
