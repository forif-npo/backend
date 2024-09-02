//package forif.univ_hanyang.attendance
//
//import forif.univ_hanyang.attendance.model.StudyAttendance
//import forif.univ_hanyang.attendance.model.StudyAttendanceId
//import org.springframework.data.jpa.repository.JpaRepository
//
//interface StudyAttendanceRepository : JpaRepository<StudyAttendance, StudyAttendanceId> {
//    fun findByStudyIdAndUserId(studyId: Int, userId: Int): StudyAttendance?
//    fun findByStudyIdAndUserIdAndWeekNum(studyId: Int, userId: Int, weekNum: Int): StudyAttendance?
//    fun findByStudyIdAndWeekNum(studyId: Int, weekNum: Int): List<StudyAttendance>
//    fun findByUserId(userId: Int): List<StudyAttendance>
//    fun findByStudyId(studyId: Int): List<StudyAttendance>
//}