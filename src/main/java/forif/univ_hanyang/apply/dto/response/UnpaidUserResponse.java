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
    private Integer primaryStudyId;
    private Integer secondaryStudyId;
    private String phoneNumber;
}
