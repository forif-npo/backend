package forif.univ_hanyang.apply.dto.response;

import lombok.Data;

@Data
public class MyApplicationResponse {
    private String timestamp;
    private AppliedStudyResponse primaryStudy;
    private AppliedStudyResponse secondaryStudy;
    private String applyPath;
}
