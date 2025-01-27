package forif.univ_hanyang.apply.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "tb_study_apply")
public class StudyApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Integer id;
    @Column(name = "study_name")
    private String name;
    private Long primaryMentorId;
    private Long secondaryMentorId;
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
    @Column(length = 50)
    private String location;
    @Column(length = 100)
    private String tag;
    private Integer acceptanceStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "studyApply", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudyApplyPlan> studyApplyPlans = new ArrayList<>();
}
