package forif.univ_hanyang.apply.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyApplyRequest {
    private String name;
    private Long primaryMentorId;
    private String primaryMentorName;
    private Long secondaryMentorId;
    private String secondaryMentorName;
    private String oneLiner;
    private String explanation;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    private String location;
    private String tag;
    private List<StudyApplyPlanRequest> studyApplyPlans;
}
