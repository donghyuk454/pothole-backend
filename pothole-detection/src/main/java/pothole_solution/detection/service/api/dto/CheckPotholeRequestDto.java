package pothole_solution.detection.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CheckPotholeRequestDto {
    private MultipartFile file;
}
