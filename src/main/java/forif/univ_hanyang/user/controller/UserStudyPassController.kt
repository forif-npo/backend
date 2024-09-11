package forif.univ_hanyang.user.controller

import forif.univ_hanyang.user.dto.request.UserStudyPassDTO
import forif.univ_hanyang.user.entity.UserStudyPass
import forif.univ_hanyang.user.service.UserStudyPassService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user-study-pass")
class UserStudyPassController(private val userStudyPassService: UserStudyPassService) {

    @PostMapping
    fun createUserStudyPass(@RequestBody userStudyPassDTO: UserStudyPassDTO): ResponseEntity<String> {
        userStudyPassService.createUserStudyPass(userStudyPassDTO)
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