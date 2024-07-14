package forif.univ_hanyang.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplyResponse {
    private Integer status;
    private String message;
}
