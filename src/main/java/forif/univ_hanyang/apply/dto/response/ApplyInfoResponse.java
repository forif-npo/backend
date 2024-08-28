package forif.univ_hanyang.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public ApplyInfoResponse(Integer applierId, String name,
                             String primaryStudyName, String secondaryStudyName,
                             String phoneNumber, String department,
                             String applyPath, String applyDate,
                             String primaryStatus, String secondaryStatus, String payYn) {
        this.userId = applierId;
        this.name = name;
        this.department = department;
        this.primaryStudyName = primaryStudyName;
        this.phoneNumber = phoneNumber;
        this.secondaryStudyName = secondaryStudyName;
        this.applyPath = applyPath;
        this.applyDate = applyDate;
        this.primaryStatus = primaryStatus;
        this.secondaryStatus = secondaryStatus;
        this.payYn = payYn;
    }
}
