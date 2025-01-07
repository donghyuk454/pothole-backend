package pothole_solution.detection.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PotholeMessageProducer {

    private final KafkaProducer<String, String> kafkaProducer;

    public void sendMessage(String topic, String key, String message) {
        kafkaProducer.send(new ProducerRecord<>(topic, key, message), (RecordMetadata metadata, Exception exception) -> {
            if (exception == null) {
                log.info("Message sent successfully to topic: {}", metadata.topic());
            } else {
                exception.printStackTrace();
                log.error("Topic : {}, Exception : {}", metadata.topic(), exception, exception);
            }
        });
    }
}
