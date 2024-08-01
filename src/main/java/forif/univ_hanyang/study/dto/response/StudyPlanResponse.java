package forif.univ_hanyang.study.dto.response;

import forif.univ_hanyang.study.entity.StudyPlan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudyPlanResponse {
    private String section;
    private String[] content;

    public static StudyPlanResponse from(StudyPlan studyPlan) {
        return StudyPlanResponse.builder()
                .section(studyPlan.getSection())
                .content(studyPlan.getContent().split(";"))
                .build();
    }
}
