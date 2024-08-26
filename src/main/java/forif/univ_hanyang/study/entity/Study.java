package forif.univ_hanyang.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer id;
    @Column(name = "study_name")
    private String name;
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
}