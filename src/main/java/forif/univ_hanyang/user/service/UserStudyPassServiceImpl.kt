package forif.univ_hanyang.user.service

import forif.univ_hanyang.user.dto.request.UserStudyPassDTO
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
    private val studyUserRepository: StudyUserRepository
) : UserStudyPassService {

    @Transactional
    override fun createUserStudyPass(userStudyPassDTO: UserStudyPassDTO) {
        val studyUsers = studyUserRepository.findAllById_StudyId(userStudyPassDTO.studyId)

        val filteredStudyUsers = studyUsers.filter { studyUser ->
            if (userStudyPassDTO.passedUserIds.isEmpty())
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "passedUserIds 가 비어있습니다.")
            if (studyUser.id.userId !in userStudyPassDTO.passedUserIds)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. ${studyUser.user.id}는 해당 스터디를 수강하지 않았습니다.")

            userStudyPassDTO.passedUserIds.contains(studyUser.id.userId)
        }

        val userStudyPasses = filteredStudyUsers.map { studyUser ->
            UserStudyPass(
                id = UserStudyPassId(studyUser.user.id, studyUser.study.id),
                user = studyUser.user,
                study = studyUser.study
            )
        }
        userStudyPassRepository.saveAll(userStudyPasses)
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