package pothole_solution.core.global.exception.kafka.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pothole_solution.core.domain.BaseTimeEntity;

@Entity
@Table(name = "FAILED_KAFKA_MESSAGE",
        indexes = {
                @Index(name = "IDX_TOPIC_MESSAGE", columnList = "TOPIC, MESSAGE_ID")
        })
@Getter
@NoArgsConstructor
public class FailedKafkaMessage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TOPIC", nullable = false, length = 50)
    private String topic;

    @Column(name = "MESSAGE_ID", length = 100)
    private String messageId;

    @Column(name = "MESSAGE_BODY", columnDefinition = "TEXT")
    private String messageBody;

    @Column(name = "EXCEPTION_MESSAGE", columnDefinition = "TEXT")
    private String exceptionMessage;

    @Builder
    public FailedKafkaMessage(String topic, String messageId, String messageBody, String exceptionMessage) {
        this.topic = topic;
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.exceptionMessage = exceptionMessage;
    }
}
