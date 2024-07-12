package fororo.univ_hanyang.study.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Map;

@Getter
@Setter
public class AllStudyInfoResponse {
    private Integer studyId;
    private Map<String, String> tags;
    private String studyName;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private String explanation;
    private Short level;
    private Integer weekDay;
    private Boolean interview;
    private String image;
    private String studyStatus;
    private Integer year;
    private Integer semester;
}
