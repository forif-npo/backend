package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RequiredArgsConstructor
@RestController
@RequestMapping("/alim-talk")
class AlimTalkController(private val alimTalkService: AlimTalkService,
                         private val userService: UserService) {

    @PostMapping
    fun sendAlimTalk(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: AlimTalkRequest
    ): Any {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        return alimTalkService.sendAlimTalk(user, request)
    }

    @GetMapping("/templates")
    fun getKakaoTemplates(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<String> {
        val user = userService.validateUserExist(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")
        return alimTalkService.getKakaoTemplates(user)
    }
}

