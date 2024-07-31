package forif.univ_hanyang.apply.dto.response;

import lombok.Data;

@Data
public class MyApplicationResponse {
    String timestamp;
    AppliedStudyResponse primaryStudy;
    AppliedStudyResponse secondaryStudy;
    String applyPath;
}
