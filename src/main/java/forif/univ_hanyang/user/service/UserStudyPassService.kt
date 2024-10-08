package forif.univ_hanyang.user.service

import forif.univ_hanyang.user.dto.request.UserStudyPassDTO
import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.entity.UserStudyPass
import org.springframework.stereotype.Service

interface UserStudyPassService{
    fun createUserStudyPass(mentor: User, userStudyPassDTO: UserStudyPassDTO)
    fun getUserStudyPassesByUserId(userId: Long): List<UserStudyPass>
    fun getUserStudyPassesByStudyId(admin: User, studyId: Int): List<UserStudyPass>
    fun deleteUserStudyPass(admin : User, userId: Long, studyId: Int)
}