package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RequiredArgsConstructor
@RestController
class AlimTalkController {
//    private val alimTalkService: AlimTalkService? = null
//    private val userService: UserService? = null

//    @GetMapping("/alim-talk")
//    fun sendAlimTalk(
//        @RequestHeader("Authorization") token: String,
//        @RequestBody request: AlimTalkRequest?
//    ): CompletableFuture<List<String>> {
//        val user = userService!!.validateUserExist(token)
//        return alimTalkService?.sendAlimTalk(request) ?: "알림톡 발송 실패"
//    }
}
