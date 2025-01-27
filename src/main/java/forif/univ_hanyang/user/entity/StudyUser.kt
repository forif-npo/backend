package forif.univ_hanyang.user.entity;

import forif.univ_hanyang.study.entity.Study
import jakarta.persistence.*

@Entity
@Table(name = "tb_study_user")
class StudyUser {
    @EmbeddedId
    lateinit var id: StudyUserId

    var certificateStatus: Int? = null

    @Column(length = 300)
    var certificateUrl: String? = null

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    lateinit var study: Study

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    lateinit var user: User
}