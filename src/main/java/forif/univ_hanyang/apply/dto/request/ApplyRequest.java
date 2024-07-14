package forif.univ_hanyang.apply.dto.request;

import lombok.Data;

@Data
public class ApplyRequest {
    private String primaryStudy;
    private String primaryIntro;
    private String secondaryStudy;
    private String secondaryIntro;
    private String career;
}
