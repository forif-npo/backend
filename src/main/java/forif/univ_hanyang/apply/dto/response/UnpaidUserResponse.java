package forif.univ_hanyang.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnpaidUserResponse {
    private Integer userId;
    private String name;
    private String primaryStudyName;
    private String secondaryStudyName;
    private String phoneNumber;
}