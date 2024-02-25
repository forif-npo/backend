package fororo.univ_hanyang.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class StudyInfoRequest {
    private Integer activityYear;
    private Integer activitySemester;
    private Integer studyId;
}

