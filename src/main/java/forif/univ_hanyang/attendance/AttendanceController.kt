package forif.univ_hanyang.attendance

import forif.univ_hanyang.attendance.dto.UserAttendanceResponse
import forif.univ_hanyang.attendance.dto.AttendanceRequest
import forif.univ_hanyang.attendance.dto.StudyAttendanceResponse
import forif.univ_hanyang.attendance.model.AttendanceStatus
import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/attendance")
class AttendanceController(
    private val attendanceService: AttendanceService,
    private val userService: UserService
) {

    @PostMapping
    fun recordAttendance(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AttendanceRequest
    ) {
        val mentor: User = userService.validateUserExist(token)
        if (mentor.authLv == 1) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }

        return attendanceService.recordAttendance(
            request.studyId,
            request.userId,
            request.weekNum,
            AttendanceStatus.valueOf(request.attendanceStatus)
        )
    }

    @GetMapping("/studies/{studyId}")
    fun getAttendanceForStudy(@PathVariable studyId: Int): List<StudyAttendanceResponse> {
        return attendanceService.getAttendanceForStudy(studyId)
    }

    @GetMapping("/users/{userId}")
    fun getAttendanceForUser(@PathVariable userId: Int): List<UserAttendanceResponse> {
        return attendanceService.getAttendanceForUser(userId)
    }
}