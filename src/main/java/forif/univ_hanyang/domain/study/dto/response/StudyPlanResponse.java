package forif.univ_hanyang.domain.study.dto.response;

import forif.univ_hanyang.domain.study.entity.StudyPlan;
import forif.univ_hanyang.common.exception.ErrorCode;
import forif.univ_hanyang.common.exception.ForifException;
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
        // studyPlan이 null인 경우 예외
        if (studyPlan == null) {
            throw new ForifException(ErrorCode.STUDY_PLAN_NOT_FOUND);
        }
        String section = studyPlan.getSection();
        String content = studyPlan.getContent();
        return StudyPlanResponse.builder()
                .section(section != null ? section : "")
                .content(content != null ? content.split(";") : new String[]{})
                .build();
    }
}


