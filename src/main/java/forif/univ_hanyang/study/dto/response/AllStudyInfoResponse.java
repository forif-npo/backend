package forif.univ_hanyang.study.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class AllStudyInfoResponse {
    private Integer id;
    private String name;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private String explanation;
    private Short level;
    private Integer weekDay;
    private String image;
    private String studyStatus;
    private String tag;
    private Integer year;
    private Integer semester;
}
