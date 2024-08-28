package forif.univ_hanyang.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPaymentStatusResponse {
    private Integer userId;
    private String name;
    private Integer studyId;
    private String studyType;
    private String priority;
    private String phoneNumber;
}
