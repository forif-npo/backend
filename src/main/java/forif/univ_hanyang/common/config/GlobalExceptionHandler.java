package forif.univ_hanyang.common.config;

import forif.univ_hanyang.common.exception.ForifException;
import forif.univ_hanyang.common.dto.response.CommonApiErrorResponse;
import forif.univ_hanyang.common.dto.response.ApiErrorData;
import forif.univ_hanyang.common.exception.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //ForifException
    //ErrCode와 dataList를 를 받아 ApiErrorResponse 반환
    @ExceptionHandler(ForifException.class)
    public ResponseEntity<CommonApiErrorResponse> handleForifException(ForifException e) {
        logger.error("ForifException 발생: [{}] {}", e.getErrorCode().getCode(), e.getMessage(), e);
        CommonApiErrorResponse response = CommonApiErrorResponse.of(
            e.getErrorCode().getCode(),
            e.getErrorCode().getMessage(),
            e.getErrorDataList()
        );
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    //폼 검증 오류 시 각 필드별 에러를 ApiErrorData 리스트로 만들어 ApiErrorResponse 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException 발생: {}", ex.getMessage(), ex);
        List<ApiErrorData> dataList = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> ApiErrorData.builder()
                .parameter(error.getField())
                .message(error.getDefaultMessage())
                .build())
            .toList();
        CommonApiErrorResponse response = CommonApiErrorResponse.of(
            ErrorCode.INVALID_INPUT.getCode(),
            ErrorCode.INVALID_INPUT.getMessage(),
            dataList
        );
        return new ResponseEntity<>(response, ErrorCode.INVALID_INPUT.getHttpStatus());
    }

    //
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonApiErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        logger.error("ResponseStatusException 발생: {}", e.getReason(), e);
        CommonApiErrorResponse response = CommonApiErrorResponse.of(
            String.valueOf(e.getStatusCode().value()),
            e.getReason(),
            null
        );
        return new ResponseEntity<>(response, e.getStatusCode());
    }
}
