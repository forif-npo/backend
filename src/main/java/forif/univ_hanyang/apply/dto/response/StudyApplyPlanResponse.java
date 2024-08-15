package forif.univ_hanyang.apply.dto.response;

import forif.univ_hanyang.apply.domain.StudyApplyPlan;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyApplyPlanResponse {
    private String section;
    private String content;

    public static List<StudyApplyPlanResponse> from(List<StudyApplyPlan> studyApplyPlans) {
        return studyApplyPlans.stream().map(studyApplyPlan -> {
            StudyApplyPlanResponse studyApplyPlanResponse = new StudyApplyPlanResponse();
            studyApplyPlanResponse.setSection(studyApplyPlan.getSection());
            studyApplyPlanResponse.setContent(studyApplyPlan.getContent());
            return studyApplyPlanResponse;
        }).toList();
    }
}
