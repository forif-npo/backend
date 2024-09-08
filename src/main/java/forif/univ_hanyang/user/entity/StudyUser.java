package forif.univ_hanyang.user.entity;

import forif.univ_hanyang.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
    @EqualsAndHashCode
    public static class StudyUserId implements Serializable {
        @Column(name = "study_id")
        private Integer studyId;

        @Column(name = "user_id")
        private Long userId;
    }

    @EmbeddedId
    private StudyUserId id;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    public Study study;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    public User user;
}