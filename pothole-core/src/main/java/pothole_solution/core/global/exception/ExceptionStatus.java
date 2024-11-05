package pothole_solution.core.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {
    // common exception
    INVALID_PARAMETER(BAD_REQUEST, 2000, "잘못된 요청이 존재합니다."),
    INVALID_URL(BAD_REQUEST, 2001, "잘못된 URL 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2002,"서버 내부 오류입니다."),
    NOT_EXISTED_FILE(NOT_EXTENDED, 2003,"존재하지 않는 파일입니다."),
    MISSING_PARAMETER(BAD_REQUEST, 2004,"놓친 파라미터가 존재합니다."),

    // session exception
    UNAUTHORIZED_SESSION(UNAUTHORIZED, 2100, "인증되지 않은 세션입니다. 로그인을 해주세요."),
    EXPIRED_SESSION(UNAUTHORIZED, 2102, "만료된 세션입니다."),
    FORBIDDEN_SESSION(FORBIDDEN, 2101, "권한이 없는 세션입니다."),

    // user exception
    DUPLICATED_EMAIL(CONFLICT, 3000, "중복된 이메일이 존재합니다."),
    NONE_USER(NOT_FOUND, 3001, "존재하지 않는 회원입니다."),
    MISMATCH_PASSWORD(UNAUTHORIZED, 3002, "비밀번호가 일치하지 않습니다."),
    NONE_ROLE(NOT_FOUND, 3003, "존재하지 않는 역할 입니다."),
    MISMATCH_ROLE(UNAUTHORIZED, 3004, "일치하지 않는 역할 입니다."),

    // pothole exception
    NONE_POTHOLE(NOT_FOUND, 4000, "존재하지 않는 포트홀입니다."),
    NONE_PROGRESS_STATUS_IMG(NOT_FOUND, 4001, "존재하지 않는 진행 상태 이미지입니다."),
    NONE_POTHOLE_HISTORY(NOT_FOUND, 4002, "존재하지 않는 포트홀 기록입니다."),
    NONE_PROGRESS_STATUS(NOT_FOUND, 4003, "존재하지 않는 진행 상태입니다."),

    // s3 exception
    FAILED_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "포트홀 이미지 업로드에 실패했습니다."),
    INVALID_POTHOLE_IMG_URL(BAD_REQUEST, 5001, "잘못된 포트홀 이미지 URL 요청입니다."),
    INVALID_POTHOLE_IMG_NAME(BAD_REQUEST, 5002, "포트홀 이미지의 이름이 존재하지 않거나 잘못되었습니다."),
    INVALID_POTHOLE_IMG(BAD_REQUEST, 5003, "포트홀 이미지가 존재하지 않거나 잘못되었습니다."),

    // Report exception
    MISMATCH_PERIOD(BAD_REQUEST, 6000, "존재하지 않는 기간입니다."),
    MISMATCH_CRITERIA(BAD_REQUEST, 6001, "존재하지 않는 기준입니다."),
    MISMATCH_REPORT_TYPE(BAD_REQUEST, 6002, "존재하지 않는 레포트 타입입니다[COUNT, HISTORY].");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
