package pothole_solution.core.global.exception.kafka.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;
import pothole_solution.core.global.exception.kafka.service.FailedKafkaMessageService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerErrorHandler implements CommonErrorHandler {

    private final FailedKafkaMessageService failedKafkaMessageService;
    @Resource(name = "defaultObjectMapper")
    private ObjectMapper objectMapper;

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        // 에러 세부 정보 로그 출력
        log.error("메시지 처리 중 오류 발생 - key: {}, value: {}, offset: {}, partition: {}, topic: {}",
                record.key(), record.value(), record.offset(), record.partition(), record.topic(), thrownException);

        try {
            // JSON 파싱하여 메시지 ID와 content 추출
            JsonNode messageNode = objectMapper.readTree(record.value().toString());

            // id, content 필드 추출
            String messageId = messageNode.has("id") ? messageNode.get("id").asText() : null;
            String content = messageNode.has("content") ? messageNode.get("content").toPrettyString() : null;

            failedKafkaMessageService.saveErrorLog(record.topic(), messageId, content, thrownException);
        } catch (IOException e) {
            // message 형식 error
            log.error("json parsing 중 에러로 보임 : {}", record.value().toString());
            failedKafkaMessageService.saveErrorLog(record.topic(), record.value().toString(), e);
        }

        return CommonErrorHandler.super.handleOne(thrownException, record, consumer, container);
    }

    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        // 에러 세부 정보 로그 출력
        log.error("Consumer 에서 오류 발생 - exception: {}, consumer: {}, subscription: {}",
                thrownException, consumer, consumer.subscription());

        CommonErrorHandler.super.handleOtherException(thrownException, consumer, container, batchListener);
    }
}
