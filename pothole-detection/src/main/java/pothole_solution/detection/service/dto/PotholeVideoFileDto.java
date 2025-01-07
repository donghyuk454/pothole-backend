package pothole_solution.detection.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class PotholeVideoFileDto {
    private String uuid;
    private MultipartFile video;
}
