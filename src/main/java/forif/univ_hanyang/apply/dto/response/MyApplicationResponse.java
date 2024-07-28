package forif.univ_hanyang.apply.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyApplicationResponse {
    LocalDateTime timestamp;
    AppliedStudyResponse primaryStudy;
    AppliedStudyResponse secondaryStudy;
    String applyPath;
}
