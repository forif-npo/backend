package forif.univ_hanyang.domain.user.service

import forif.univ_hanyang.domain.study.repository.StudyRepository
import forif.univ_hanyang.domain.user.dto.request.StudyUserDTO
import forif.univ_hanyang.domain.user.entity.StudyUser
import forif.univ_hanyang.domain.user.entity.User
import forif.univ_hanyang.domain.user.repository.StudyUserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
open class StudyUserServiceImpl(
    private val studyUserRepository: StudyUserRepository,
    private val studyRepository: StudyRepository
) : StudyUserService {

    @Transactional
    override fun changeUsersToPassed(authUser: User, studyUserDTO: StudyUserDTO) {
        validateUserAuthorization(authUser, studyUserDTO.studyId)

        val studyUsers = studyUserRepository.findAllById_StudyId(studyUserDTO.studyId)
        val filteredStudyUsers = studyUsers.filter { studyUser ->
            studyUserDTO.passedUsers.any { passedUser ->
                studyUser.id.userId == passedUser.id
            }
        }

        val passedUsers = filteredStudyUsers.map { studyUser ->
            val certificateUrl = studyUserDTO.passedUsers
                .first { it.id == studyUser.id.userId }
                .certificateUrl
            studyUser.apply {
                certificateStatus = 1 // 수료
                this.certificateUrl = certificateUrl
            }
        }

        studyUserRepository.saveAll(passedUsers)
    }

    private fun validateUserAuthorization(authUser: User, studyId: Int) {
        if (authUser.authLv == 1) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }

        studyRepository.findById(studyId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다.")
        }
    }

    override fun getStudyUserByUserId(userId: Long): List<StudyUser> {
        return studyUserRepository.findAllById_UserId(userId)
    }

    override fun getStudyUserByStudyId(admin: User, studyId: Int): List<StudyUser> {
        if(admin.authLv == 1){
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }
        return studyUserRepository.findAllById_StudyId(studyId)
    }

    @Transactional
    override fun revertPassedStatus(admin: User, studyId: Int, userId: Long) {
        if(admin.authLv<3){
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }
        val studyUser = studyUserRepository.findById_StudyIdAndId_UserId(studyId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다.")

        studyUser.certificateStatus = 0
        studyUser.certificateUrl = null
    }
}