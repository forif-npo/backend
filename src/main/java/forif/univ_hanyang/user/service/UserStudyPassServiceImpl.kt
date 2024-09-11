package forif.univ_hanyang.user.service

import forif.univ_hanyang.study.repository.StudyRepository
import forif.univ_hanyang.user.dto.request.UserStudyPassDTO
import forif.univ_hanyang.user.entity.StudyUser
import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.entity.UserStudyPass
import forif.univ_hanyang.user.entity.UserStudyPassId
import forif.univ_hanyang.user.repository.StudyUserRepository
import forif.univ_hanyang.user.repository.UserStudyPassRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
open class UserStudyPassServiceImpl(
    private val userStudyPassRepository: UserStudyPassRepository,
    private val studyUserRepository: StudyUserRepository,
    private val studyRepository: StudyRepository
) : UserStudyPassService {

    @Transactional
    override fun createUserStudyPass(mentor: User, userStudyPassDTO: UserStudyPassDTO) {
        validateMentorAuthorization(mentor, userStudyPassDTO.studyId)
        validatePassedUserIds(userStudyPassDTO.passedUserIds)

        val studyUsers = studyUserRepository.findAllById_StudyId(userStudyPassDTO.studyId)
        val filteredStudyUsers = filterValidStudyUsers(studyUsers, userStudyPassDTO.passedUserIds)

        val userStudyPasses = createUserStudyPasses(filteredStudyUsers)
        userStudyPassRepository.saveAll(userStudyPasses)
    }

    private fun validateMentorAuthorization(mentor: User, studyId: Int) {
        if (mentor.authLv == 1) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }

        val study = studyRepository.findById(studyId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다.")
        }

        if (study.primaryMentorName != mentor.name && study.secondaryMentorName?.let { it != mentor.name } == true) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "해당 스터디의 멘토가 아닙니다.")
        }
    }

    private fun validatePassedUserIds(passedUserIds: Set<Long>) {
        if (passedUserIds.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "요청 값이 비어있습니다.")
        }
    }

    private fun filterValidStudyUsers(studyUsers: List<StudyUser>, passedUserIds: Set<Long>): List<StudyUser> {
        val studyUserMap = studyUsers.associateBy { it.user.id }

        val invalidUserIds = passedUserIds - studyUserMap.keys
        if (invalidUserIds.isNotEmpty()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "잘못된 요청입니다. 다음 유저들은 스터디에 포함되지 않습니다: $invalidUserIds"
            )
        }
        return passedUserIds.mapNotNull { studyUserMap[it] }
    }

    private fun createUserStudyPasses(studyUsers: List<StudyUser>): List<UserStudyPass> {
        return studyUsers.map { studyUser ->
            UserStudyPass(
                id = UserStudyPassId(studyUser.user.id, studyUser.study.id),
                user = studyUser.user,
                study = studyUser.study
            )
        }
    }

    override fun getUserStudyPassesByUserId(userId: Long): List<UserStudyPass> {
        return userStudyPassRepository.findByIdUserId(userId)
    }

    override fun getUserStudyPassesByStudyId(studyId: Int): List<UserStudyPass> {
        return userStudyPassRepository.findByIdStudyId(studyId)
    }

    @Transactional
    override fun deleteUserStudyPass(userId: Long, studyId: Int) {
        val id = UserStudyPassId(userId, studyId)

        if (!userStudyPassRepository.existsById(id)) {
            throw EntityNotFoundException("UserStudyPass with userId $userId and studyId $studyId not found.")
        }

        userStudyPassRepository.deleteById(UserStudyPassId(userId, studyId))
    }
}