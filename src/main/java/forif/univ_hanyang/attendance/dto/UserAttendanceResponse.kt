package forif.univ_hanyang.attendance.dto

data class UserAttendanceResponse(
    val studyName: String,
    val weekNum: Int,
    val attendanceStatus: String,
    val studyDate: String
)
