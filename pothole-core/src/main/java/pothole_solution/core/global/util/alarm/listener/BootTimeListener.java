package pothole_solution.core.global.util.alarm.listener;

import com.slack.api.model.block.LayoutBlock;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import pothole_solution.core.global.util.alarm.slack.SlackMessageFormatter;
import pothole_solution.core.global.util.alarm.slack.SlackService;

import java.time.Duration;
import java.util.List;

import static pothole_solution.core.global.util.alarm.slack.constant.SlackConstant.*;

public class BootTimeListener implements SpringApplicationRunListener {
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.ready(context, timeTaken);

        SlackService slackService = context.getBean(SlackService.class);

        long millis = timeTaken.toMillis();
        String startupTime = String.valueOf(millis / 1000.0);

        boolean isLocalServer = context.getEnvironment().matchesProfiles("manager-local | worker-local | consumer-local");

        if (!isLocalServer) {
            boolean isManagerDevServer = context.getEnvironment().matchesProfiles("manager-dev");
            String serverName = isManagerDevServer ? MANAGER_SERVER : WORKER_SERVER;

            List<LayoutBlock> layoutBlocks = new SlackMessageFormatter().buildBootMessageFormat(serverName, startupTime, true);
            slackService.sendMessage(POTHOLE_SERVER_DEPLOY, POTHOLE_SERVER_DEPLOY_PREVIEW_MSG, layoutBlocks);
        }
    }
}
