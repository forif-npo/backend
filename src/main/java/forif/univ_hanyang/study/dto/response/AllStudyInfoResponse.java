package forif.univ_hanyang.study.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
public class AllStudyInfoResponse {
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
    private String oneLiner;
    private Integer actYear;
    private Integer actSemester;
}
