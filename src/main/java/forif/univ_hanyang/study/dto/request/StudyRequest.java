package forif.univ_hanyang.study.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
public class StudyRequest {
    private String name;
    private String primaryMentorName;
    private String SecondaryMentorName;
    private String oneLiner;
    private String startTime;
    private String endTime;
    private Integer weekDay;
    private Integer difficulty;
    private String explanation;
    private String image;
    private String location;
    private String tag;
    private List<String> sections;
    private List<String> contents;
}


