package pothole_solution.detection.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pothole_solution.common.consumer.BasicConsumer;
import pothole_solution.core.domain.pothole.dto.message.creation.PotholeCreationContent;
import pothole_solution.core.domain.pothole.dto.message.creation.PotholeCreationMessage;
import pothole_solution.core.domain.pothole.dto.message.image.Base64MultipartFile;
import pothole_solution.core.global.exception.CustomException;
import pothole_solution.detection.message.PotholeDetectionContent;
import pothole_solution.detection.message.PotholeDetectionMessage;
import pothole_solution.detection.producer.PotholeMessageProducer;
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
    private final PotholeMessageProducer potholeMessageProducer;

    private static final String GROUP_NAME = "pothole-detection-group";
    private static final String TOPIC = "pothole-detection";
    private static final Integer TIMEOUT = 10000;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");

    public PotholeDetectionConsumer(PotholeDetectionEventService eventService,
                                    PotholeCheckApiService potholeCheckApiService,
                                    PotholeMessageProducer potholeMessageProducer) {
        super(GROUP_NAME, TOPIC, TIMEOUT);
        this.eventService = eventService;
        this.potholeCheckApiService = potholeCheckApiService;
        this.potholeMessageProducer = potholeMessageProducer;
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
        PotholeDetectionContent detectionContent = message.getContent();
        String uuid = String.valueOf(UUID.randomUUID());
        String base64Video = detectionContent.getVideo();
        PotholeVideoFileDto videoDto = new PotholeVideoFileDto(uuid, convertBase64ToMultipartFile(base64Video, createFileName(now, messageId, uuid)));

        // 포트홀일 경우만 저장
        boolean isPothole = potholeCheckApiService.isPothole(videoDto.getVideo());
        if (isPothole) {
            // 이벤트 저장
            eventService.saveEventWithImages(message.getContent(), videoDto);

            // 포트홀 생성 매시지 Produce
            PotholeCreationMessage creationMessage = getPotholeCreationMessage(message, detectionContent, videoDto);
            producePotholeCreationMessage(creationMessage);
        }
    }

    private static PotholeCreationMessage getPotholeCreationMessage(PotholeDetectionMessage message,
                                                                    PotholeDetectionContent detectionContent,
                                                                    PotholeVideoFileDto videoDto) {
        float importance = 50.5f;
        float danger = 40.2f;

        PotholeCreationContent creationContent = new PotholeCreationContent(detectionContent.getLat(),
                detectionContent.getLon(), importance, danger, videoDto.getVideo().getName());

        return new PotholeCreationMessage(message.getId(), creationContent);
    }

    private MultipartFile convertBase64ToMultipartFile(String base64, String fileName) {
        return new Base64MultipartFile(base64, fileName, "video/mp4");
    }

    private String createFileName(String today, String messageId, String uuid) {
        return "pot_detection_" + messageId + "_" + uuid + "_" + today + ".mp4";
    }

    private void producePotholeCreationMessage(PotholeCreationMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            potholeMessageProducer.sendMessage("pothole-creation", message.getId(), jsonMessage);
        } catch (JsonProcessingException e) {
            throw CustomException.INVALID_PARAMETER;
        }
    }
}
