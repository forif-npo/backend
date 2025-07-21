package forif.univ_hanyang.domain.attendance.dto

data class StudyAttendanceResponse (
    val menteeName: String,
    val menteeDepartment: String,
    val menteePhoneNumber: String,
    val weekNum: Int,
    val attendanceStatus: String,
    val studyDate: String,


)