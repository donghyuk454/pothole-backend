package pothole_solution.core.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pothole_solution.core.global.util.response.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handlePotholeException(CustomException e) {
        ExceptionStatus status = e.getStatus();
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(status));
    }

    /**
     * @Valid exception handler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidException() {
        CustomException exception = CustomException.INVALID_PARAMETER;
        return ResponseEntity
                .status(exception.getStatus().getHttpStatus())
                .body(new BaseResponse<>(exception.getStatus()));
    }
}
