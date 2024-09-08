package forif.univ_hanyang.attendance.dto

data class AttendanceRequest (
    val studyId: Int,
    val userId: Long,
    val weekNum: Int,
    val attendanceStatus: String
)