package forif.univ_hanyang.apply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import forif.univ_hanyang.apply.domain.StudyApply;
import forif.univ_hanyang.apply.domain.StudyApplyPlan;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyApplyResponse {
    private Integer id;
    private String name;
    private Integer primaryMentorId;
    private String primaryMentorName;
    private Integer secondaryMentorId;
    private String secondaryMentorName;
    private String primaryMentorEmail;
    private String secondaryMentorEmail;
    private String primaryMentorPhoneNumber;
    private String secondaryMentorPhoneNumber;
    private String oneLiner;
    private String explanation;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    private String location;
    private String tag;
    private List<StudyApplyPlan> studyApplyPlans;

    public static StudyApplyResponse from(StudyApply studyApply) {
        StudyApplyResponse studyApplyResponse = new StudyApplyResponse();
        studyApplyResponse.setId(studyApply.getId());
        studyApplyResponse.setName(studyApply.getName());
        studyApplyResponse.setPrimaryMentorId(studyApply.getPrimaryMentorId());
        studyApplyResponse.setPrimaryMentorName(studyApply.getPrimaryMentorName());
        studyApplyResponse.setSecondaryMentorId(studyApply.getSecondaryMentorId());
        studyApplyResponse.setSecondaryMentorName(studyApply.getSecondaryMentorName());
        studyApplyResponse.setOneLiner(studyApply.getOneLiner());
        studyApplyResponse.setExplanation(studyApply.getExplanation());
        studyApplyResponse.setWeekDay(studyApply.getWeekDay());
        studyApplyResponse.setStartTime(studyApply.getStartTime());
        studyApplyResponse.setEndTime(studyApply.getEndTime());
        studyApplyResponse.setDifficulty(studyApply.getDifficulty());
        studyApplyResponse.setLocation(studyApply.getLocation());
        studyApplyResponse.setTag(studyApply.getTag());
        studyApplyResponse.setStudyApplyPlans(studyApply.getStudyApplyPlans());
        return studyApplyResponse;
    }
}