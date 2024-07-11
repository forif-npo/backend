package fororo.univ_hanyang.user.dto.response;

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
    private Integer userId;
    private String phoneNumber;
    private String department;
    private String userAuthorization;
    private Integer currentStudyId;
    private String image;
    private Integer myStudy;
    private Set<Integer> passedStudyId;
}
