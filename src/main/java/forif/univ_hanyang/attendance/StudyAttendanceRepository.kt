package forif.univ_hanyang.attendance

import forif.univ_hanyang.attendance.model.StudyAttendance
import forif.univ_hanyang.attendance.model.StudyAttendanceId
import org.springframework.data.jpa.repository.JpaRepository

interface StudyAttendanceRepository : JpaRepository<StudyAttendance, StudyAttendanceId> {
    fun findByIdUserId(userId: Int): List<StudyAttendance>
    fun findByIdStudyId(studyId: Int): List<StudyAttendance>
}