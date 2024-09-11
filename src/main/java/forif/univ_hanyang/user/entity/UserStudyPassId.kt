package forif.univ_hanyang.user.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserStudyPassId(
    private val userId: Long = 0,
    private val studyId: Int = 0
) : Serializable

