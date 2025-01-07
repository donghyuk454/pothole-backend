package pothole_solution.core.global.exception.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pothole_solution.core.global.exception.kafka.entity.FailedKafkaMessage;
import pothole_solution.core.global.exception.kafka.repository.FailedKafkaMessageRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class FailedKafkaMessageService {

    private final FailedKafkaMessageRepository failedKafkaMessageRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveErrorLog(String topic, String messageId, String content, Exception exception) {
        FailedKafkaMessage failedKafkaMessage = FailedKafkaMessage.builder()
                .topic(topic)
                .messageId(messageId)
                .messageBody(content)
                .exceptionMessage(exception.getMessage())
                .build();
        failedKafkaMessageRepository.save(failedKafkaMessage);

        log.warn("Failed kafka message is saved | createdId: {}, topic: {}, messageId: {}",
                failedKafkaMessage.getId(), failedKafkaMessage.getTopic(), failedKafkaMessage.getMessageId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveErrorLog(String topic, String content, Exception exception) {
        FailedKafkaMessage failedKafkaMessage = FailedKafkaMessage.builder()
                .topic(topic)
                .messageId("")
                .messageBody(content)
                .exceptionMessage(exception.getMessage())
                .build();
        failedKafkaMessageRepository.save(failedKafkaMessage);

        log.warn("Failed kafka message is saved | createdId: {}, topic: {}, messageId: {}",
                failedKafkaMessage.getId(), failedKafkaMessage.getTopic(), failedKafkaMessage.getMessageId());
    }
}

