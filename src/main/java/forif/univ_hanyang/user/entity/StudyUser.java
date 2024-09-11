package forif.univ_hanyang.user.entity;

import forif.univ_hanyang.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_study_user")
public class StudyUser {
    @Embeddable
    @Setter
    @EqualsAndHashCode
    public static class StudyUserId implements Serializable {
        @Column(name = "study_id")
        private Integer studyId;

        @Column(name = "user_id")
        private Long userId;

        public Integer getStudyId() {
            return studyId;
        }

        public Long getUserId() {
            return userId;
        }
    }

    @EmbeddedId
    private StudyUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    public Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    public User user;

    public StudyUserId getId() {
        return id;
    }

    public Study getStudy() {
        return study;
    }

    public User getUser() {
        return user;
    }
}