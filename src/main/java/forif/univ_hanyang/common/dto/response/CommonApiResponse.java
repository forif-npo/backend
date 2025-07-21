package forif.univ_hanyang.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {
    private long timestamp;
    public T body;
    //private 으로 하면 AlimTalkController에서 인식을 못함 (임시방편)

    public static <T> CommonApiResponse<T> of(T body) {
        return CommonApiResponse.<T>builder()
                .timestamp(System.currentTimeMillis())
                .body(body)
                .build();
    }
}
