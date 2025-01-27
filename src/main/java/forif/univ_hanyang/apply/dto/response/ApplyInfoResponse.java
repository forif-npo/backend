package forif.univ_hanyang.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyInfoResponse {
    private Long userId;
    private String name;
    private String department;
    private String primaryStudyName;
    private String secondaryStudyName;
    private String phoneNumber;
    private String intro;
    private String applyPath;
    private Integer payStatus;
    private Integer primaryStatus;
    private Integer secondaryStatus;
    private String applyDate;

    public ApplyInfoResponse(Long applierId, String name,
                             String primaryStudyName, String secondaryStudyName,
                             String phoneNumber, String department,
                             String applyPath, String applyDate,
                             Integer primaryStatus, Integer secondaryStatus, Integer payStatus) {
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
        this.payStatus = payStatus;
    }
}
