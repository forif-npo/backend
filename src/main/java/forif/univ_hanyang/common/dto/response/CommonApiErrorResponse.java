package forif.univ_hanyang.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import java.util.List;

@Getter
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiErrorResponse {
    private String code;
    private String message;
    private List<ApiErrorData> dataList;

    public static CommonApiErrorResponse of(String code, String message, List<ApiErrorData> dataList) {
        return CommonApiErrorResponse.builder()
                .code(code)
                .message(message)
                .dataList(dataList)
                .build();
    }

    public static CommonApiErrorResponse of(String code, String message) {
        return CommonApiErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}