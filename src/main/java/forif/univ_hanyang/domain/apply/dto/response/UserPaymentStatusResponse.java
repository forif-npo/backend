package forif.univ_hanyang.domain.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPaymentStatusResponse {
    private Long userId;
    private String name;
    private Integer studyId;
    private String studyType;
    private String priority;
    private String phoneNumber;
}
