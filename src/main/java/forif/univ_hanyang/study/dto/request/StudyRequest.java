package forif.univ_hanyang.study.dto.request;

import lombok.Data;

import java.sql.Time;
import java.util.List;

@Data
public class StudyRequest {
    private String name;
    private Integer mentorId;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private Boolean interview;
    private Integer weekDay;
    private Short level;
    private String explanation;
    private String image;
    private String location;
    private String tag;
    private List<String> weeklyPlan;
}


