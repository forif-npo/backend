package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RequiredArgsConstructor
@RestController
class AlimTalkController(private val alimTalkService: AlimTalkService,
                         private val userService: UserService) {

    @PostMapping("/alim-talk")
    fun sendAlimTalk(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AlimTalkRequest
    ): Any {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        return alimTalkService.sendAlimTalk(user, request)
    }
}

