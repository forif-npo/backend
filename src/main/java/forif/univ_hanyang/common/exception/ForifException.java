package forif.univ_hanyang.common.exception;

import java.util.List;

import forif.univ_hanyang.common.dto.response.ApiErrorData;

import lombok.Getter;

@Getter
public class ForifException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private final List<ApiErrorData> errorDataList;

    public ForifException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorDataList = null;
    }

    public ForifException(ErrorCode errorCode, List<ApiErrorData> errorDataList) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorDataList = errorDataList;
    }
    
    public ForifException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.errorDataList = null;
    }
    
    public ForifException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.errorDataList = null;
    }
    
    public ForifException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
        this.errorDataList = null;
    }
} 