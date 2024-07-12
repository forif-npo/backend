package fororo.univ_hanyang.study.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;


@Getter
@Setter
public class StudyUserResponse {
    private String studyName;
    private String mentorName;
    private Integer weekDay;
    private Time startTime;
    private Time endTime;
    private String reference;
    private String location;
    private List<String> weeklyPlans;
}