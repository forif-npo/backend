package fororo.univ_hanyang.study.dto.request;

import lombok.Data;

import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
public class StudyRequest {
    private Set<String> tag;
    private String studyName;
    private Integer mentorId;
    private String mentorEmail;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private Boolean interview;
    private Integer weekDay;
    private String goal;
    private Short level;
    private String explanation;
    private String image;
    private String location;
    private String maximumUsers;
    private String reference;
    private List<String> weeklyPlan;
    private String conditions;
}


