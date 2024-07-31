package forif.univ_hanyang.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserInfoResponse {
    private String email;
    private String userName;
    private String department;
    private Integer id;
    private String phoneNumber;
    private Integer currentStudyId;
    private Set<Integer> passedStudyId;
}
