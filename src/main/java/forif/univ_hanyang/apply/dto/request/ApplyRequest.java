package forif.univ_hanyang.apply.dto.request;

import lombok.Data;

@Data
public class ApplyRequest {
    private Integer primaryStudy;
    private Integer secondaryStudy;
    private String primaryIntro;
    private String secondaryIntro;
    private String applyPath;
}
