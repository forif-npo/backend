package forif.univ_hanyang.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrentTermResponse {
    private Integer year;
    private Integer semester;
}
