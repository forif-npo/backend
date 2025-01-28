package forif.univ_hanyang.user.controller

import forif.univ_hanyang.jwt.RequireJWT
import forif.univ_hanyang.user.dto.request.StudyUserDTO
import forif.univ_hanyang.user.entity.StudyUser
import forif.univ_hanyang.user.service.StudyUserService
import forif.univ_hanyang.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Tag(name = "스터디 수료", description = "유저의 스터디 수료를 관리하는 API")
@RestController
@RequestMapping("/user-study-pass")
class StudyUserController(
    private val studyUserService: StudyUserService,
    private val userService: UserService
) {

    @Operation(summary = "유저 스터디 수료로 변경", description = "유저를 스터디 수료로 추가합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "스터디 수료 생성 성공"),
            ApiResponse(responseCode = "400", description = "스터디 수료 생성 실패")
        ]
    )
    @PostMapping
    fun changeUsersToPassed(
        @RequestBody studyUserDTO: StudyUserDTO,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<String> {
        val authUser = userService.validateUserExist(token)
            ?: return ResponseEntity("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST)
        studyUserService.changeUsersToPassed(authUser, studyUserDTO)
        return ResponseEntity("Creation Success", HttpStatus.CREATED)
    }

    @Operation(summary = "유저의 스터디 수료 조회", description = "유저의 스터디 수료를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "스터디 수료 조회 성공"),
            ApiResponse(responseCode = "400", description = "스터디 수료 조회 실패"),
            ApiResponse(responseCode = "403", description = "권한이 없습니다.")
        ]
    )
    @RequireJWT
    @GetMapping("/users/{userId}")
    fun getStudyUserByUserId(@PathVariable userId: Long): ResponseEntity<List<StudyUser>> {
        val userStudyPasses = studyUserService.getStudyUserByUserId(userId)
        return ResponseEntity(userStudyPasses, HttpStatus.OK)
    }

    @Operation(summary = "스터디의 유저 수료 조회", description = "스터디의 유저 수료를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "유저 수료 조회 성공"),
            ApiResponse(responseCode = "400", description = "유저 수료 조회 실패"),
            ApiResponse(responseCode = "403", description = "권한이 없습니다.")
        ]
    )
    @GetMapping("/studies/{studyId}")
    fun getStudyUserByStudyId(
        @PathVariable studyId: Int,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<StudyUser>> {
        val admin = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 유저가 없습니다.")
        val userStudyPasses = studyUserService.getStudyUserByStudyId(admin, studyId)
        return ResponseEntity(userStudyPasses, HttpStatus.OK)
    }

    @Operation(summary = "유저의 스터디 수료 삭제", description = "유저의 스터디 수료를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "스터디 수료 삭제 성공"),
            ApiResponse(responseCode = "400", description = "스터디 수료 삭제 실패"),
            ApiResponse(responseCode = "403", description = "권한이 없습니다.")
        ]
    )
    @DeleteMapping("/users/{userId}/studies/{studyId}")
    fun deleteStudyUser(
        @PathVariable userId: Long,
        @PathVariable studyId: Int,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Unit> {
        val admin = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 유저가 없습니다.")
        studyUserService.revertPassedStatus(admin, studyId, userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}