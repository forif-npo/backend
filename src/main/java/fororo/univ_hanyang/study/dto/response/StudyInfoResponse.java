package fororo.univ_hanyang.study.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class StudyInfoResponse {
    private Integer studyId;
    private Integer mentorId;
    private String mentorEmail;
    private String studyName;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private String goal;
    private Short level;
    private String explanation;
    private Integer weekDay;
    private Boolean interview;
    private String image;
    private String location;
    private String maximumUsers;
    private String reference;
    private List<String> weeklyPlans;
    private String conditions;
}

