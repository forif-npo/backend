package forif.univ_hanyang.domain.alimtalk

import forif.univ_hanyang.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import forif.univ_hanyang.common.dto.response.CommonApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse 
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Tag(name = "알림톡", description = "알림톡 관련 API")
@RestController
@RequestMapping("/alim-talk")
class AlimTalkController(
    private val alimTalkService: AlimTalkService,
    private val userService: UserService
) {

    @Operation(
        summary = "알림톡 발송",
        description = "알림톡을 발송합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "알림톡 발송 성공"),
            ApiResponse(responseCode = "400", description = "사용자 정보를 찾을 수 없습니다."),
            ApiResponse(responseCode = "403", description = "알림 톡을 보낼 권한이 없습니다.")
        ]
    )
    @PostMapping
    fun sendAlimTalk(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AlimTalkRequest
    ): ResponseEntity<CommonApiResponse<Any>> {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        val result = alimTalkService.sendAlimTalk(user, request)
        return ResponseEntity.ok(CommonApiResponse.of(result))
    }

    @Operation(
        summary = "알림톡 템플릿 조회",
        description = "알림톡 템플릿을 조회합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "알림톡 템플릿 조회 성공"),
            ApiResponse(responseCode = "400", description = "사용자 정보를 찾을 수 없습니다."),
            ApiResponse(responseCode = "403", description = "알림 톡 템플릿을 조회할 권한이 없습니다.")
        ]
    )
    @GetMapping("/templates")
    fun getKakaoTemplates(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<CommonApiResponse<Any>> {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        val result = alimTalkService.getKakaoTemplates(user).body
        return ResponseEntity.ok(CommonApiResponse.of(result))
    }

    @Operation(
        summary = "알림톡 로그 조회",
        description = "알림톡 로그를 조회합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "알림톡 로그 조회 성공"),
            ApiResponse(responseCode = "400", description = "사용자 정보를 찾을 수 없습니다."),
            ApiResponse(responseCode = "403", description = "알림 톡 로그를 조회할 권한이 없습니다.")
        ]
    )
    @GetMapping("/logs")
    fun getAlimTalkLogs(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<CommonApiResponse<Any>> {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        val result = alimTalkService.getAlimTalkLogs(user).body
        return ResponseEntity.ok(CommonApiResponse.of(result))
    }
}

