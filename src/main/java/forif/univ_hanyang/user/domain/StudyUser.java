package forif.univ_hanyang.user.domain;

import forif.univ_hanyang.study.domain.Study;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_study_user")
public class StudyUser {
    @Embeddable
    @Getter
    @Setter
    public static class StudyUserId implements Serializable {
        @Column(name = "study_id")
        private Integer studyId;

        @Column(name = "user_id")
        private Integer userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudyUserId that = (StudyUserId) o;
            return Objects.equals(studyId, that.studyId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studyId, userId);
        }
    }

    @EmbeddedId
    private StudyUserId id;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    private Study study;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
}