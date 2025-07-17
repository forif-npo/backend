package forif.univ_hanyang.common.config;

import forif.univ_hanyang.common.exception.ForifException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ForifException.class)
    public ResponseEntity<String> handleForifException(ForifException e) {
        logger.error("ForifException 발생: [{}] {}", e.getErrorCode().getCode(), e.getMessage(), e);
        return new ResponseEntity<>("오류 발생: " + e.getMessage(), e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e instanceof ResponseStatusException rse) {
            logger.error("ResponseStatusException 발생: {}", rse.getReason(), rse);
            return new ResponseEntity<>("오류 발생: " + rse.getReason(), rse.getStatusCode());
        }
        logger.error("예외 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>("오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
}
