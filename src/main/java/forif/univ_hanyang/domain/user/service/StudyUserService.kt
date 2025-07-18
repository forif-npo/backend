package forif.univ_hanyang.domain.user.service

import forif.univ_hanyang.domain.user.dto.request.StudyUserDTO
import forif.univ_hanyang.domain.user.entity.StudyUser
import forif.univ_hanyang.domain.user.entity.User

interface StudyUserService{
    fun changeUsersToPassed(authUser: User, studyUserDTO: StudyUserDTO)
    fun getStudyUserByUserId(userId: Long): List<StudyUser>
    fun getStudyUserByStudyId(admin: User, studyId: Int): List<StudyUser>
    fun revertPassedStatus(admin : User, studyId: Int, userId: Long)
}