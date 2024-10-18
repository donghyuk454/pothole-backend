package pothole_solution.common.constant;

public class KafkaConstant {
    public static final String BOOTSTRAP_SERVERS_KEY = "bootstrap.servers";
    public static final String GROUP_ID_KEY = "group.id";
    public static final String KEY_DESERIALIZER_KEY = "key.deserializer";
    public static final String KEY_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String VALUE_DESERIALIZER_KEY = "value.deserializer";
    public static final String VALUE_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";
}
