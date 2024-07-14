package forif.univ_hanyang.study.entity;

import forif.univ_hanyang.clubInfo.entity.ClubInfo;
import forif.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "study")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "mentor_id")
    private Integer mentorId;
    @Column(name = "mentor_name")
    private String mentorName;
    private String explanation;
    @Column(name = "week_day")
    private Integer weekDay;
    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;
    private Short level;
    @Column(name = "club_id")
    private Integer clubId;
    private String image;
    private String location;
    private String tag;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WeeklyPlan> weeklyPlans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "club_id", insertable = false, updatable = false)
    private ClubInfo clubInfo;
}