package pothole_solution.core.domain.pothole.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicMessage<T> {
    private String id;
    private T content;
}
