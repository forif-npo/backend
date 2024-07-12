package fororo.univ_hanyang.study.entity;

import fororo.univ_hanyang.clubInfo.entity.ClubInfo;
import fororo.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "study")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Integer studyId;

    @Column(name = "study_name")
    private String studyName;

    @Column(name = "mentor_id")
    private Integer mentorId;

    private String mentorEmail;

    @Column(name = "mentor_name")
    private String mentorName;

    @Column(name = "explanation", length = 801)
    private String explanation;

    @Column(name = "interview")
    private Boolean interview;

    @Column(name = "goal", length = 501)
    private String goal;

    @Column(name = "week_day")
    private Integer weekDay;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    @Column(name = "level")
    private Short level;

    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "image")
    private String image;

    private String location;

    private String maximumUsers;

    private String reference;

    private String conditions;

    private String tag;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WeeklyPlan> weeklyPlans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "club_id", insertable = false, updatable = false)
    private ClubInfo clubInfo;
}