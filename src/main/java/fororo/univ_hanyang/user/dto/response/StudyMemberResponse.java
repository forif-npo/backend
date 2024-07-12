package fororo.univ_hanyang.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyMemberResponse {
    private Integer id;
    private String email;
    private String name;
    private String department;
    private String phoneNumber;

}
