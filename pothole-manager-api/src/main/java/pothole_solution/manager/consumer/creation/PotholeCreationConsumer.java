package pothole_solution.manager.consumer.creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pothole_solution.core.domain.pothole.dto.message.creation.PotholeCreationMessage;
import pothole_solution.core.domain.pothole.dto.request.ReqPotRegPotMngrServDto;
import pothole_solution.manager.service.PotholeManagerService;

@Component
@RequiredArgsConstructor
public class PotholeCreationConsumer {

    private final PotholeManagerService potholeManagerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "pothole-creation", groupId = "pothole-creation-group", concurrency = "3")
    public void consume(String message) throws JsonProcessingException {
        // pothole 생성 dto 로 변환
        PotholeCreationMessage potholeCreationMessage = objectMapper.readValue(message, PotholeCreationMessage.class);
        ReqPotRegPotMngrServDto potholeRegisterDto = potholeCreationMessage.getContent().toCreationDto();

        // pothole 생성
        potholeManagerService.registerPothole(potholeRegisterDto, potholeCreationMessage.getContent().getImageLink());
    }
}