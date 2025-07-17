package forif.univ_hanyang.domain.study.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class StudyInfoResponse {
    private Integer id;
    private String name;
    private String primaryMentorName;
    private String secondaryMentorName;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    private String explanation;
    private Integer weekDay;
    private String image;
    private String location;
    private String tag;
    private List<StudyPlanResponse> studyPlans;
    private String oneLiner;
    private Integer actYear;
    private Integer actSemester;
}

