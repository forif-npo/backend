package forif.univ_hanyang.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class StudyUserId(
    @Column(name = "study_id")
    val studyId: Int = 0,

    @Column(name = "user_id")
    val userId: Long = 0
) : Serializable