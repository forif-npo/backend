package fororo.univ_hanyang.cert.entity;

import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue_info")
public class IssueInfo {

    @Getter
    @Setter
    @Embeddable
    public static class IssueInfoId implements Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "study_id")
        private Integer studyId;
    }

    @EmbeddedId
    private IssueInfoId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    private Study study;

    @Column(name = "attendance_pass")
    private Boolean attendancePass;

    @Column(name = "hackathon_pass")
    private Boolean hackathonPass;
}
