package fororo.univ_hanyang.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudyResponse {
    private Integer status;
    private String message;
}
