package forif.univ_hanyang.apply.domain;

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
    private Integer primaryMentorId;
    private String primaryMentorName;
    private Integer secondaryMentorId;
    private String secondaryMentorName;
    private String oneLiner;
    private String explanation;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer difficulty;
    private String location;
    private String tag;

    @JsonManagedReference
    @OneToMany(mappedBy = "studyApply", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudyApplyPlan> studyApplyPlans = new ArrayList<>();
}
