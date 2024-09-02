//package forif.univ_hanyang.attendance
//
//import forif.univ_hanyang.attendance.model.AttendanceStatus
//import forif.univ_hanyang.attendance.model.StudyAttendance
//import forif.univ_hanyang.user.entity.User
//import forif.univ_hanyang.user.service.UserService
//import org.springframework.http.HttpStatus
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.server.ResponseStatusException
//
//@RestController
//@RequestMapping("/attendance")
//class AttendanceController(private val attendanceService: AttendanceService,
//                           private val userService: UserService) {
//
//    @PostMapping("/record")
//    fun recordAttendance(
//        @RequestHeader("Authorization") token: String,
//        @RequestBody request: AttendanceRequest
//    ): StudyAttendance {
//        val user: User = userService.validateUserExist(token)
//        if(user.authLv == 1){
//            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
//        }
//
//        return attendanceService.recordAttendance(
//            request.studyId,
//            request.userId,
//            request.weekNum,
//            AttendanceStatus.valueOf(request.attendanceStatus)
//        )
//    }
//
//    @GetMapping("/study/{studyId}")
//    fun getAttendanceForStudy(@PathVariable studyId: Int): List<StudyAttendance>{
//        return attendanceService.getAttendanceForStudy(studyId)
//    }
//
//    @GetMapping("/user/{userId}")
//    fun getAttendanceForUser(@PathVariable userId: Int): List<StudyAttendance> {
//        return attendanceService.getAttendanceForUser(userId)
//    }
//
//
////    @GetMapping("/study/{studyId}/week/{weekNum}")
////    fun getAttendanceForStudyWeek(@PathVariable studyId: Int, @PathVariable weekNum: Int): List<StudyAttendance> {
////        return attendanceService.getAttendanceForStudyWeek(studyId, weekNum)
////    }
//}