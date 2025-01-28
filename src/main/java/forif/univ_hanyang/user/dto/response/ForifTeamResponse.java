package forif.univ_hanyang.user.dto.response;

import forif.univ_hanyang.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ForifTeamResponse {
    private Integer actYear;
    private Integer actSemester;
    private String userTitle;
    private String clubDepartment;
    private String introTag;
    private String selfIntro;
    private String profImgUrl;
    private User user;
}
