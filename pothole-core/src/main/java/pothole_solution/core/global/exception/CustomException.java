package pothole_solution.core.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomException extends RuntimeException {
    private final ExceptionStatus status;

    // common exception
    public static final CustomException INVALID_PARAMETER       = new CustomException(ExceptionStatus.INVALID_PARAMETER);
    public static final CustomException INVALID_URL             = new CustomException(ExceptionStatus.INVALID_URL);
    public static final CustomException INTERNAL_SERVER_ERROR   = new CustomException(ExceptionStatus.INTERNAL_SERVER_ERROR);
    public static final CustomException NOT_EXISTED_FILE        = new CustomException(ExceptionStatus.NOT_EXISTED_FILE);
    public static final CustomException MISSING_PARAMETER       = new CustomException(ExceptionStatus.MISSING_PARAMETER);

    // session exception
    public static final CustomException UNAUTHORIZED_SESSION    = new CustomException(ExceptionStatus.UNAUTHORIZED_SESSION);
    public static final CustomException EXPIRED_SESSION         = new CustomException(ExceptionStatus.EXPIRED_SESSION);
    public static final CustomException FORBIDDEN_SESSION       = new CustomException(ExceptionStatus.FORBIDDEN_SESSION);

    // user exception
    public static final CustomException DUPLICATED_EMAIL        = new CustomException(ExceptionStatus.DUPLICATED_EMAIL);
    public static final CustomException NONE_USER               = new CustomException(ExceptionStatus.NONE_USER);
    public static final CustomException MISMATCH_PASSWORD       = new CustomException(ExceptionStatus.MISMATCH_PASSWORD);
    public static final CustomException NONE_ROLE               = new CustomException(ExceptionStatus.NONE_ROLE);
    public static final CustomException MISMATCH_ROLE           = new CustomException(ExceptionStatus.MISMATCH_ROLE);

    // pothole exception
    public static final CustomException NONE_POTHOLE                    = new CustomException(ExceptionStatus.NONE_POTHOLE);
    public static final CustomException NONE_PROGRESS_STATUS_IMG        = new CustomException(ExceptionStatus.NONE_PROGRESS_STATUS_IMG);
    public static final CustomException NONE_POTHOLE_HISTORY            = new CustomException(ExceptionStatus.NONE_POTHOLE_HISTORY);
    public static final CustomException NONE_PROGRESS_STATUS            = new CustomException(ExceptionStatus.NONE_PROGRESS_STATUS);

    // s3 exception
    public static final CustomException FAILED_UPLOAD               = new CustomException(ExceptionStatus.FAILED_UPLOAD);
    public static final CustomException INVALID_POTHOLE_IMG_URL     = new CustomException(ExceptionStatus.INVALID_POTHOLE_IMG_URL);
    public static final CustomException INVALID_POTHOLE_IMG_NAME    = new CustomException(ExceptionStatus.INVALID_POTHOLE_IMG_NAME);
    public static final CustomException INVALID_POTHOLE_IMG         = new CustomException(ExceptionStatus.INVALID_POTHOLE_IMG);

    // Report Exception
    public static final CustomException MISMATCH_PERIOD               = new CustomException(ExceptionStatus.MISMATCH_PERIOD);
    public static final CustomException MISMATCH_CRITERIA               = new CustomException(ExceptionStatus.MISMATCH_CRITERIA);
    public static final CustomException MISMATCH_REPORT_TYPE               = new CustomException(ExceptionStatus.MISMATCH_REPORT_TYPE);
}
