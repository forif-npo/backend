package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.CompletableFuture

@RequiredArgsConstructor
@RestController
class AlimTalkController {
    private val alimTalkService: AlimTalkService? = null
    private val userService: UserService? = null

    @GetMapping("/alim-talk")
    fun sendAlimTalk(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AlimTalkRequest
    ): Any {
        val user = userService?.validateUserExist(token) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        return alimTalkService?.sendAlimTalk(user, request)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "알림톡 발송 실패")

    }
}
