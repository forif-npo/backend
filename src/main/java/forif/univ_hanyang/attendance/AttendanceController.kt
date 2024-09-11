package forif.univ_hanyang.attendance

import forif.univ_hanyang.attendance.dto.UserAttendanceResponse
import forif.univ_hanyang.attendance.dto.AttendanceRequest
import forif.univ_hanyang.attendance.dto.StudyAttendanceResponse
import forif.univ_hanyang.attendance.model.AttendanceStatus
import forif.univ_hanyang.jwt.RequireJWT
import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Tag(name = "출석", description = "출석 관련 API")
@RestController
@RequestMapping("/attendance")
class AttendanceController(
    private val attendanceService: AttendanceService,
    private val userService: UserService
) {

    @Operation(
        summary = "출석 기록",
        description = "스터디의 특정 주차에 대한 사용자의 출석을 기록합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "출석 기록 성공"),
            ApiResponse(responseCode = "400", description = "해당 사용자가 스터디에 가입되어 있지 않습니다."),
            ApiResponse(responseCode = "403", description = "권한이 없습니다.")
        ]
    )
    @PostMapping
    fun recordAttendance(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AttendanceRequest
    ): ResponseEntity<Unit> {
        val mentor: User = userService.validateUserExist(token)
        if (mentor.authLv == 1) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }

        return ResponseEntity(
            attendanceService.recordAttendance(
                request.studyId,
                request.userId,
                request.weekNum,
                AttendanceStatus.valueOf(request.attendanceStatus)),
            HttpStatus.OK)
    }

    @RequireJWT
    @GetMapping("/studies/{studyId}")
    fun getAttendanceForStudy(@PathVariable studyId: Int): ResponseEntity<List<StudyAttendanceResponse>> {
        return ResponseEntity(attendanceService.getAttendanceForStudy(studyId), HttpStatus.OK)
    }

    @RequireJWT
    @GetMapping("/users/{userId}")
    fun getAttendanceForUser(@PathVariable userId: Long): ResponseEntity<List<UserAttendanceResponse>> {
        return ResponseEntity(attendanceService.getAttendanceForUser(userId), HttpStatus.OK)
    }
}