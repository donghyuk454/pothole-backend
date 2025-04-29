package pothole_solution.detection.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pothole_solution.core.infra.s3.ImageService;
import pothole_solution.detection.entity.PotholeDetectionEvent;
import pothole_solution.detection.message.PotholeDetectionContent;
import pothole_solution.detection.repository.PotholeDetectionEventRepository;
import pothole_solution.detection.service.dto.PotholeVideoFileDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PotholeDetectionEventService {

    private final PotholeDetectionEventRepository eventRepository;
    private final ImageService imageService;

    public void saveEventWithImages(PotholeDetectionContent content, PotholeVideoFileDto video) {
        // event 저장
        PotholeDetectionEvent event = PotholeDetectionEvent.builder()
                .lat(content.getLat())
                .lon(content.getLon())
                .fileName(video.getVideo().getName())
                .build();

        eventRepository.save(event);

        // image 를 s3 에 저장
        imageService.uploadImages(List.of(video.getVideo()), "pothole-detection-events");
    }
}
