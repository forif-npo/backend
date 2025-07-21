package forif.univ_hanyang.domain.study.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyPatchRequest {
    private String name;
    private String primaryMentorName;
    private String SecondaryMentorName;
    private String oneLiner;
    private String startTime;
    private String endTime;
    private Integer weekDay;
    private Integer difficulty;
    private String explanation;
    private String location;
    private String tag;
    private List<StudyPlanRequest> studyPlans;
}


