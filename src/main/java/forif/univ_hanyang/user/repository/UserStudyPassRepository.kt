package forif.univ_hanyang.user.repository

import forif.univ_hanyang.user.entity.UserStudyPass
import forif.univ_hanyang.user.entity.UserStudyPassId
import org.springframework.data.jpa.repository.JpaRepository

interface UserStudyPassRepository : JpaRepository<UserStudyPass, UserStudyPassId> {
    fun findByIdUserId(userId: Long): List<UserStudyPass>
    fun findByIdStudyId(studyId: Int): List<UserStudyPass>
}