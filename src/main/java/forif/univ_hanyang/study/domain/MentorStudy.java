package forif.univ_hanyang.study.domain;

import forif.univ_hanyang.user.domain.User;
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
@Table(name = "tb_mentor_study")
public class MentorStudy {
    @Embeddable
    @Getter
    @Setter
    public static class MentorStudyId implements Serializable {
        private Integer mentorId;
        private Integer studyId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MentorStudyId that = (MentorStudyId) o;
            return Objects.equals(mentorId, that.mentorId) && Objects.equals(studyId, that.studyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mentorId, studyId);
        }
    }

    @EmbeddedId
    MentorStudyId id;

    private Integer mentorNum;

    public void changeUserAuthLv(User user) {
        if (user.getAuthLv() == 1)
            user.setAuthLv(2);
    }

    @ManyToOne
    @MapsId("mentorId")
    @JoinColumn(name = "mentor_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    private Study study;
}
