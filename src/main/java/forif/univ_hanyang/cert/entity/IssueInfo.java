package forif.univ_hanyang.cert.entity;

import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IssueInfoId that = (IssueInfoId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(studyId, that.studyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, studyId);
        }
    }

    @EmbeddedId
    private IssueInfoId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Study study;

    @Column(name = "attendance_pass")
    private Boolean attendancePass;

    @Column(name = "hackathon_pass")
    private Boolean hackathonPass;
}
