package pothole_solution.core.global.util.alarm.listener;

import com.slack.api.model.block.LayoutBlock;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import pothole_solution.core.global.util.alarm.slack.SlackMessageFormatter;
import pothole_solution.core.global.util.alarm.slack.SlackService;

import java.io.IOException;
import java.util.List;

import static pothole_solution.core.global.util.alarm.slack.constant.SlackConstant.*;

public class BootFailureListener implements ApplicationListener<ApplicationFailedEvent> {
    @Override
    public void onApplicationEvent(@NotNull ApplicationFailedEvent event) {
        try {
            SlackService slackService = new SlackService(getSlackToken());

            if (!isLocalServer()) {
                boolean isManagerDevServer = new ClassPathResource("application-manager-dev.yml").exists();
                boolean isDetectionDevServer = new ClassPathResource("application-detection-dev.yml").exists();

                String serverName = isManagerDevServer ? MANAGER_SERVER : (isDetectionDevServer ? DETECTION_SERVER : WORKER_SERVER);

                List<LayoutBlock> layoutBlocks = new SlackMessageFormatter().buildBootMessageFormat(serverName, FAILURE_STARTUP_TIME, false);
                slackService.sendMessage(POTHOLE_SERVER_DEPLOY, POTHOLE_SERVER_DEPLOY_PREVIEW_MSG, layoutBlocks);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getSlackToken() throws IOException {
        // 환경 설정 로드
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addLast(new ResourcePropertySource("classpath:application-core-dev.yml"));

        return environment.getProperty("slack.token");
    }

    private boolean isLocalServer() {
        boolean isManagerLocalServer = new ClassPathResource("application-manager-local.yml").exists();
        boolean isWorkerLocalServer = new ClassPathResource("application-worker-local.yml").exists();
        boolean isDetectionLocalServer = new ClassPathResource("application-detection-local.yml").exists();

        return isManagerLocalServer || isWorkerLocalServer || isDetectionLocalServer;
    }
}
