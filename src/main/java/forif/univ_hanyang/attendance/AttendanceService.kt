package forif.univ_hanyang.attendance

import forif.univ_hanyang.attendance.dto.UserAttendanceResponse
import forif.univ_hanyang.attendance.dto.StudyAttendanceResponse
import forif.univ_hanyang.attendance.model.AttendanceStatus
import forif.univ_hanyang.attendance.model.StudyAttendance
import forif.univ_hanyang.attendance.model.StudyAttendanceId
import forif.univ_hanyang.user.entity.StudyUser
import forif.univ_hanyang.user.repository.StudyUserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class AttendanceService(
    private val studyAttendanceRepository: StudyAttendanceRepository,
    private val studyUserRepository: StudyUserRepository
) {
    fun recordAttendance(studyId: Int, userId: Int, weekNum: Int, status: AttendanceStatus) {
        val studyUser: StudyUser = studyUserRepository.findById_StudyIdAndId_UserId(studyId, userId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 스터디에 가입되어 있지 않습니다.")

        val attendanceId = StudyAttendanceId(studyId, userId, weekNum)
        val attendance = StudyAttendance(
            id = attendanceId,
            studyDate = LocalDate.now(java.time.ZoneId.of("Asia/Seoul")).toString(),
            study = studyUser.study,
            user = studyUser.user,
            attendanceStatus = status
        )

        studyAttendanceRepository.save(attendance)
    }

    fun getAttendanceForStudy(studyId: Int): List<StudyAttendanceResponse> {
        return studyAttendanceRepository.findByIdStudyId(studyId)
            .map { attendance ->
                StudyAttendanceResponse(
                    menteeName = attendance.user.name,
                    menteeDepartment = attendance.user.department,
                    menteePhoneNumber = attendance.user.phoneNumber,
                    weekNum = attendance.id.weekNum,
                    attendanceStatus = attendance.attendanceStatus.toString(),
                    studyDate = attendance.studyDate?:""
                )
            }
    }

    fun getAttendanceForUser(userId: Int): List<UserAttendanceResponse> {
        return studyAttendanceRepository.findByIdUserId(userId)
            .map { attendance ->
                UserAttendanceResponse(
                    studyName = attendance.study.name,
                    weekNum = attendance.id.weekNum,
                    attendanceStatus = attendance.attendanceStatus.toString(),
                    studyDate = attendance.studyDate?:""
                )
            }
    }
}