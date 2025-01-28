package forif.univ_hanyang.study.entity;

import forif.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
    @EqualsAndHashCode
    public static class MentorStudyId implements Serializable {
        private Long mentorId;
        private Integer studyId;
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
