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
    private Integer id;
    private String name;
    private Integer mentorId;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private Short level;
    private String explanation;
    private Integer weekDay;
    private String image;
    private String location;
    private String tag;
    private List<String> weeklyPlans;
}

