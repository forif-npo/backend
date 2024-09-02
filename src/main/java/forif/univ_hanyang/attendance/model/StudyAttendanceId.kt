package forif.univ_hanyang.attendance.model

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class StudyAttendanceId(
    val studyId: Int = 0,
    val userId: Int = 0,
    val weekNum: Int = 0
) : Serializable