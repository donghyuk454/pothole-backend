package pothole_solution.manager.health;

import com.slack.api.model.block.LayoutBlock;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pothole_solution.core.global.util.alarm.slack.SlackMessageFormatter;
import pothole_solution.core.global.util.alarm.slack.SlackService;
import pothole_solution.core.global.util.response.BaseResponse;
import pothole_solution.manager.health.dto.RespHlthChkDto;

import java.time.LocalDateTime;
import java.util.List;

import static pothole_solution.core.global.util.alarm.slack.constant.SlackConstant.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pothole/v1/manager/detection")
public class DetectionCheckController {

    private final SlackService slackService;

    @GetMapping("/start")
    public BaseResponse<RespHlthChkDto> startDetection(@RequestParam(name = "alarm", required = false) Boolean alarm,
                                                    HttpServletRequest request) {
        log.info("pothole detection start");

        if (alarm != null && alarm) {
            List<LayoutBlock> layoutBlocks = new SlackMessageFormatter()
                    .buildStartDetectionMessage(LocalDateTime.now(), request);
            slackService.sendMessage(POTHOLE_SERVER_DEPLOY, POTHOLE_DETECTION_START_PREVIEW_MSG, layoutBlocks);
        }

        return new BaseResponse<>(new RespHlthChkDto(true));
    }
}
