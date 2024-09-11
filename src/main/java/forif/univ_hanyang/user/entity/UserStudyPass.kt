package forif.univ_hanyang.user.entity

import forif.univ_hanyang.study.entity.Study
import jakarta.persistence.*

@Entity
@Table(name = "tb_user_study_pass")
class UserStudyPass(
    @EmbeddedId
    val id: UserStudyPassId,

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id")
    var study: Study,

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    var user: User
) {
    constructor() : this(
        id = UserStudyPassId(),
        study = Study(),
        user = User()
    )
}
