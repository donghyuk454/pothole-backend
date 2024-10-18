package pothole_solution.detection.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.common.consumer.BasicConsumer;
import pothole_solution.common.message.image.Base64MultipartFile;
import pothole_solution.core.global.exception.CustomException;
import pothole_solution.detection.message.PotholeDetectionMessage;
import pothole_solution.detection.service.PotholeDetectionEventService;
import pothole_solution.detection.service.api.PotholeCheckApiService;
import pothole_solution.detection.service.dto.PotholeVideoFileDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class PotholeDetectionConsumer extends BasicConsumer<String, String, PotholeDetectionMessage> {

    private final PotholeDetectionEventService eventService;
    private final PotholeCheckApiService potholeCheckApiService;

    private static final String GROUP_NAME = "pothole-detection-group";
    private static final String TOPIC = "pothole-detection";
    private static final Integer TIMEOUT = 10000;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");

    public PotholeDetectionConsumer(PotholeDetectionEventService eventService, PotholeCheckApiService potholeCheckApiService) {
        super(GROUP_NAME, TOPIC, TIMEOUT);
        this.eventService = eventService;
        this.potholeCheckApiService = potholeCheckApiService;
    }

    @Override
    protected PotholeDetectionMessage process(ConsumerRecord<String, String> message) throws RuntimeException {
        log.info(message.key());

        try {
            PotholeDetectionMessage detectionMessage = objectMapper.readValue(message.value(), PotholeDetectionMessage.class);
            log.info("message : {}", detectionMessage);
            return detectionMessage;
        } catch (JsonProcessingException e) {
            throw CustomException.INVALID_PARAMETER;
        }
    }

    @Override
    protected void write(PotholeDetectionMessage message) throws RuntimeException {
        String now = LocalDateTime.now().format(formatter);
        String messageId = message.getId();

        // dto 객체로 변환
        String uuid = String.valueOf(UUID.randomUUID());
        String base64Video = message.getContent().getVideo();
        PotholeVideoFileDto videoDto = new PotholeVideoFileDto(uuid, convertBase64ToMultipartFile(base64Video, createFileName(now, messageId, uuid)));

        // pothole 일 경우만 저장
        boolean isPothole = potholeCheckApiService.isPothole(videoDto.getVideo());
        if (isPothole) {
            eventService.saveEventWithImages(message.getContent(), videoDto);
        }
    }

    private MultipartFile convertBase64ToMultipartFile(String base64, String fileName) {
        return new Base64MultipartFile(base64, fileName, "video/mp4");
    }

    private String createFileName(String today, String messageId, String uuid) {
        return "pot_detection_" + messageId + "_" + uuid + "_" + today + ".mp4";
    }
}
