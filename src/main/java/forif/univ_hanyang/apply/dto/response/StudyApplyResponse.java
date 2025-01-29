package forif.univ_hanyang.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import forif.univ_hanyang.apply.entity.StudyApply;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyApplyResponse {
    private Integer id;
    private String name;
    private Long primaryMentorId;
    private String primaryMentorName;
    private String primaryMentorEmail;
    private String primaryMentorPhoneNumber;
    private Long secondaryMentorId;
    private String secondaryMentorName;
    private String secondaryMentorEmail;
    private String secondaryMentorPhoneNumber;
    private String oneLiner;
    private String explanation;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    private String location;
    private String tag;
    private Integer status;
    private List<StudyApplyPlanResponse> studyApplyPlans;

    public static StudyApplyResponse from(StudyApply studyApply) {
        StudyApplyResponse studyApplyResponse = new StudyApplyResponse();
        studyApplyResponse.setId(studyApply.getId());
        studyApplyResponse.setName(studyApply.getName());
        studyApplyResponse.setPrimaryMentorId(studyApply.getPrimaryMentor().getId());
        studyApplyResponse.setPrimaryMentorName(studyApply.getPrimaryMentor().getName());
        studyApplyResponse.setPrimaryMentorEmail(studyApply.getPrimaryMentor().getEmail());
        studyApplyResponse.setPrimaryMentorEmail(studyApply.getPrimaryMentor().getPhoneNumber());
        studyApplyResponse.setOneLiner(studyApply.getOneLiner());
        studyApplyResponse.setExplanation(studyApply.getExplanation());
        studyApplyResponse.setWeekDay(studyApply.getWeekDay());
        studyApplyResponse.setStartTime(studyApply.getStartTime());
        studyApplyResponse.setEndTime(studyApply.getEndTime());
        studyApplyResponse.setDifficulty(studyApply.getDifficulty());
        studyApplyResponse.setLocation(studyApply.getLocation());
        studyApplyResponse.setTag(studyApply.getTag());
        studyApplyResponse.setStatus(studyApply.getAcceptanceStatus());
        studyApplyResponse.setStudyApplyPlans(StudyApplyPlanResponse.from(studyApply.getStudyApplyPlans()));
        return studyApplyResponse;
    }
}
