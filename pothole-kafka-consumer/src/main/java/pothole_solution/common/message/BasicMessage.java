package pothole_solution.common.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public abstract class BasicMessage<T> {
    private String id;
    private T content;
}
