package forif.univ_hanyang.user.controller

import forif.univ_hanyang.user.dto.request.UserStudyPassDTO
import forif.univ_hanyang.user.entity.UserStudyPass
import forif.univ_hanyang.user.service.UserService
import forif.univ_hanyang.user.service.UserStudyPassService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "스터디 수료", description = "유저의 스터디 수료를 관리하는 API")
@RestController
@RequestMapping("/user-study-pass")
class UserStudyPassController(
    private val userStudyPassService: UserStudyPassService,
    private val userService: UserService
) {

    @Operation(summary = "유저의 스터디 수료 생성", description = "유저를 스터디 수료로 추가합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "스터디 수료 생성 성공"),
            ApiResponse(responseCode = "400", description = "스터디 수료 생성 실패")
        ]
    )
    @PostMapping
    fun createUserStudyPass(
        @RequestBody userStudyPassDTO: UserStudyPassDTO,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<String> {
        val mentor = userService.validateUserExist(token)
            ?: return ResponseEntity("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST)
        userStudyPassService.createUserStudyPass(mentor, userStudyPassDTO)
        return ResponseEntity("Creation Success", HttpStatus.CREATED)
    }

    @GetMapping("/users/{userId}")
    fun getUserStudyPassesByUserId(@PathVariable userId: Long): ResponseEntity<List<UserStudyPass>> {
        val userStudyPasses = userStudyPassService.getUserStudyPassesByUserId(userId)
        return ResponseEntity(userStudyPasses, HttpStatus.OK)
    }

    @GetMapping("/studies/{studyId}")
    fun getUserStudyPassesByStudyId(@PathVariable studyId: Int): ResponseEntity<List<UserStudyPass>> {
        val userStudyPasses = userStudyPassService.getUserStudyPassesByStudyId(studyId)
        return ResponseEntity(userStudyPasses, HttpStatus.OK)
    }

    @DeleteMapping("/users/{userId}/studies/{studyId}")
    fun deleteUserStudyPass(@PathVariable userId: Long, @PathVariable studyId: Int): ResponseEntity<Unit> {
        userStudyPassService.deleteUserStudyPass(userId, studyId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}