//package forif.univ_hanyang.attendance
//
//import forif.univ_hanyang.attendance.model.AttendanceStatus
//import forif.univ_hanyang.attendance.model.StudyAttendance
//import forif.univ_hanyang.attendance.model.StudyAttendanceId
//import forif.univ_hanyang.study.repository.StudyRepository
//import forif.univ_hanyang.user.entity.StudyUser
//import forif.univ_hanyang.user.repository.StudyUserRepository
//import forif.univ_hanyang.user.repository.UserRepository
//import okhttp3.Response
//import org.springframework.http.HttpStatus
//import org.springframework.stereotype.Service
//import org.springframework.web.server.ResponseStatusException
//
//@Service
//class AttendanceService(
//    private val studyAttendanceRepository: StudyAttendanceRepository,
//    private val studyUserRepository: StudyUserRepository
//) {
//    fun recordAttendance(studyId: Int, userId: Int, weekNum: Int, status: AttendanceStatus): StudyAttendance {
//        val studyUser: StudyUser = studyUserRepository.findById_StudyIdAndId_UserId(studyId, userId)
//            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 스터디에 가입되어 있지 않습니다.")
//
//        val attendanceId = StudyAttendanceId(studyId, userId, weekNum)
//        val attendance = StudyAttendance(id = attendanceId, study = studyUser.study, user = studyUser.user, attendanceStatus = status)
//
//        return studyAttendanceRepository.save(attendance)
//    }
//
//    fun getAttendanceForStudy(studyId: Int): List<StudyAttendance> {
//        return studyAttendanceRepository.findByStudyId(studyId)
//    }
//
//    fun getAttendanceForUser(userId: Int): List<StudyAttendance> {
//        return studyAttendanceRepository.findByUserId(userId)
//    }


//    fun getAttendanceForStudyWeek(studyId: Int, weekNum: Int): List<StudyAttendance> {
//        return studyAttendanceRepository.findByStudyIdAndWeekNum(studyId, weekNum)
//    }

//}