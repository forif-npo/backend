package forif.univ_hanyang.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyInfoResponse {
    private Integer userId;
    private String name;
    private String department;
    private String primaryStudyName;
    private String secondaryStudyName;
    private String phoneNumber;
    private String intro;
    private String applyPath;
    private String payYn;
    private String primaryStatus;
    private String secondaryStatus;
    private String applyDate;
}
