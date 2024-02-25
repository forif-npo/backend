package fororo.univ_hanyang.user.entity;

import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_study")
public class UserStudy {
    @Embeddable
    @Getter
    @Setter
    public static class UserStudyId implements Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "study_id")
        private Integer studyId;
    }

    @EmbeddedId
    private UserStudyId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    private Study study;
}