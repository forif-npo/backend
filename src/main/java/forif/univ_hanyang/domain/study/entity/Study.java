package forif.univ_hanyang.domain.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_study")
public class Study {
    @Id
    @Column(name = "study_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "study_name", length = 50)
    public String name;
    @Column(length = 50)    
    private String primaryMentorName;
    @Column(length = 50)
    private String secondaryMentorName;
    @Column(length = 300)
    private String oneLiner;
    @Column(length = 5000)
    private String explanation;
    private Integer weekDay;
    @Column(length = 50)
    private String startTime;
    @Column(length = 50)
    private String endTime;
    private Integer difficulty;
    @Column(name = "img_url", length = 300)
    private String image;
    @Column(length = 50)
    private String location;
    @Column(length = 100)
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