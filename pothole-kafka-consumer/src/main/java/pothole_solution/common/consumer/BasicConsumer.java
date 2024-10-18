package pothole_solution.common.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import pothole_solution.common.message.BasicMessage;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static pothole_solution.common.constant.KafkaConstant.*;

@Slf4j
@RequiredArgsConstructor
public abstract class BasicConsumer<K, V, T extends BasicMessage> {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    private final String groupName;
    private final String topic;
    private final Integer timeout;

    protected KafkaConsumer<K, V> consumer;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_KEY, bootstrapServers);
        props.put(GROUP_ID_KEY, groupName);

        props.put(KEY_DESERIALIZER_KEY, KEY_DESERIALIZER_VALUE);
        props.put(VALUE_DESERIALIZER_KEY, VALUE_DESERIALIZER_VALUE);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        pollMessages();
    }

    protected abstract T process(ConsumerRecord<K, V> message) throws RuntimeException;
    protected abstract void write(T message) throws RuntimeException;

    private void pollMessages() {
        while (true) {
            ConsumerRecords<K, V> messages = consumer.poll(Duration.ofSeconds(timeout));
            try {
                // message 처리
                messages.forEach(message -> {
                    write(process(message));
                });
                // message commit
                consumer.commitAsync();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @PreDestroy
    public void cleanup() {
        consumer.close();
    }
}
