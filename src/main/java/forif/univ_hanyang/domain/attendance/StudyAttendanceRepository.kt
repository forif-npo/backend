package forif.univ_hanyang.domain.attendance

import forif.univ_hanyang.domain.attendance.model.StudyAttendance
import forif.univ_hanyang.domain.attendance.model.StudyAttendanceId
import org.springframework.data.jpa.repository.JpaRepository

interface StudyAttendanceRepository : JpaRepository<StudyAttendance, StudyAttendanceId> {
    fun findByIdUserId(userId: Long): List<StudyAttendance>
    fun findByIdStudyId(studyId: Int): List<StudyAttendance>
}