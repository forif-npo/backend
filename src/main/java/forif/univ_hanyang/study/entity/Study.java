package forif.univ_hanyang.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_study")
public class Study {
    @Id
    @Column(name = "study_id")
    private Integer id;
    @Column(name = "study_name")
    public String name;
    private String primaryMentorName;
    private String secondaryMentorName;
    private String oneLiner;
    private String explanation;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    @Column(name = "img_url")
    private String image;
    private String location;
    private String tag;
    private Integer actYear;
    private Integer actSemester;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudyPlan> studyPlans = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrimaryMentorName() {
        return primaryMentorName;
    }

    public String getSecondaryMentorName() {
        return secondaryMentorName;
    }

    public String getOneLiner() {
        return oneLiner;
    }

    public String getExplanation() {
        return explanation;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getTag() {
        return tag;
    }

    public Integer getActYear() {
        return actYear;
    }

    public Integer getActSemester() {
        return actSemester;
    }

    public List<StudyPlan> getStudyPlans() {
        return studyPlans;
    }
}