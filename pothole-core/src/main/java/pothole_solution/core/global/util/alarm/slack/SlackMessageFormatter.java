package pothole_solution.core.global.util.alarm.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.model.block.LayoutBlock;
import org.springframework.core.io.ClassPathResource;
import pothole_solution.core.global.util.alarm.slack.dto.SlkPrInfoSlkMsgFmtrDto;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static pothole_solution.core.global.util.alarm.slack.constant.SlackConstant.NO_PR_INFO_MSG;

public class SlackMessageFormatter {
    public List<LayoutBlock> buildBootMessageFormat(String serverName, String startupTime, boolean isSuccess) {
        // 애플리케이션 실행 상태 확인
        String bootStatusEmoji = isSuccess ? ":white_check_mark:" : ":x:";

        SlkPrInfoSlkMsgFmtrDto slkPrInfoSlkMsgFmtrDto = getPRInfo();

        String prAuthor = slkPrInfoSlkMsgFmtrDto.getAuthor();
        String prTitle = slkPrInfoSlkMsgFmtrDto.getTitle();
        String prUrl = slkPrInfoSlkMsgFmtrDto.getUrl();

        String prUrlMsg = prUrl.isEmpty() ? NO_PR_INFO_MSG : "<" + prUrl + "|PR 보기>";

        List<LayoutBlock> layoutBlocks = new ArrayList<>();

        leaveSpace(layoutBlocks);

        // header
        layoutBlocks.add(header(headerBlockBuilder -> headerBlockBuilder.text(plainText(":newspaper:  포트홀 서버 배포 알림  :newspaper:"))));
        layoutBlocks.add(divider());

        // title
        layoutBlocks.add(section(section -> section.text(markdownText(":placard:  *Title*"))));
        layoutBlocks.add(section(section -> section.text(markdownText(prTitle))));
        layoutBlocks.add(divider());

        // server name
        layoutBlocks.add(section(section -> section.text(markdownText(":open_file_folder:  *배포 서버*"))));
        layoutBlocks.add(section(section -> section.text(markdownText("*" + serverName +"*"))));
        layoutBlocks.add(divider());

        // other info
        layoutBlocks.add(section(section -> section.text(markdownText(":mag_right:  *기타 정보*"))));
        layoutBlocks.add(section(section -> section.text(markdownText("*배포 성공 여부*  |  " + bootStatusEmoji))));
        layoutBlocks.add(section(section -> section.text(markdownText("*배포 소요 시간 (단위: 초)*  |  `" + startupTime + "`"))));
        layoutBlocks.add(section(section -> section.text(markdownText("*PR 생성자*  |  " + prAuthor))));
        layoutBlocks.add(section(section -> section.text(markdownText("*PR URL*  |  " + prUrlMsg))));

        leaveSpace(layoutBlocks);
        leaveSpace(layoutBlocks);

        return layoutBlocks;
    }

    private void leaveSpace(List<LayoutBlock> layoutBlocks) {
        layoutBlocks.add(section(section -> section.text(markdownText(" "))));
    }

    private SlkPrInfoSlkMsgFmtrDto getPRInfo() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ClassPathResource prInfoJson = new ClassPathResource("pr_info.json");

            SlkPrInfoSlkMsgFmtrDto slkPrInfoSlkMsgFmtrDto = objectMapper.readValue(prInfoJson.getInputStream(), SlkPrInfoSlkMsgFmtrDto.class);

            if (slkPrInfoSlkMsgFmtrDto.getAuthor().isEmpty()) {
                slkPrInfoSlkMsgFmtrDto.changeNoAuthorValue();
            }

            if (slkPrInfoSlkMsgFmtrDto.getTitle().isEmpty()) {
                slkPrInfoSlkMsgFmtrDto.changeNoTitleValue();
            }

            return slkPrInfoSlkMsgFmtrDto;

        } catch (IOException e) {
            e.printStackTrace();

            return SlkPrInfoSlkMsgFmtrDto.builder()
                    .author(NO_PR_INFO_MSG)
                    .title(NO_PR_INFO_MSG)
                    .url("")
                    .build();
        }
    }

    public List<LayoutBlock> buildStartDetectionMessage(LocalDateTime startupTime, HttpServletRequest request) {
        List<LayoutBlock> layoutBlocks = new ArrayList<>();

        leaveSpace(layoutBlocks);

        // header
        layoutBlocks.add(header(headerBlockBuilder -> headerBlockBuilder.text(plainText(":newspaper:  포트홀 탐지 차량 알림  :newspaper:"))));
        layoutBlocks.add(divider());

        // body

        // title
        layoutBlocks.add(section(section -> section.text(markdownText(":placard:  *Title*"))));
        layoutBlocks.add(section(section -> section.text(markdownText("포트홀 탐지 차량 출발"))));
        layoutBlocks.add(divider());

        // time
        String time = startupTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        layoutBlocks.add(section(section -> section.text(markdownText(":placard:  *시작 시간*"))));
        layoutBlocks.add(section(section -> section.text(markdownText(time))));
        layoutBlocks.add(divider());

        // time
        layoutBlocks.add(section(section -> section.text(markdownText(":placard:  *탐지 차량 IP*"))));
        layoutBlocks.add(section(section -> section.text(markdownText(getClientIp(request)))));
        layoutBlocks.add(divider());

        // time
        String statusEmoji = ":white_check_mark:";
        layoutBlocks.add(section(section -> section.text(markdownText("*성공 여부*  |  " + statusEmoji))));
        layoutBlocks.add(section(section -> section.text(markdownText("*담당자*     |  " + "신채호"))));

        leaveSpace(layoutBlocks);
        leaveSpace(layoutBlocks);

        return layoutBlocks;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
