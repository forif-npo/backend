package fororo.univ_hanyang.study.dto.request;

import fororo.univ_hanyang.study.entity.StudyType;
import fororo.univ_hanyang.study.entity.Tag;
import lombok.Data;

import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
public class StudyCreateRequest {
    private Set<String> tag;
    private String studyName;
    private Integer mentorId;
    private String mentorEmail;
    private String mentorName;
    private Time startTime;
    private Time endTime;
    private Boolean interview;
    private String date;
    private String goal;
    private Short level;
    private String explanation;
    private String image;
    private String location;
    private String maximumUsers;
    private String reference;
    private List<String> weeklyPlan;
    private String conditions;
    private String studyType;
}


