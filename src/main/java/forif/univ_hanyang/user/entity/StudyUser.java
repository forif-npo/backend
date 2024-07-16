package forif.univ_hanyang.user.entity;

import forif.univ_hanyang.study.entity.Study;
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
@Table(name = "study_user")
public class StudyUser {
    @Embeddable
    @Getter
    @Setter
    public static class StudyUserId implements Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "study_id")
        private Integer studyId;
    }

    @EmbeddedId
    private StudyUserId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Study study;
}