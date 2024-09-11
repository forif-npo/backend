package forif.univ_hanyang.user.dto.request

data class UserStudyPassDTO(
    val studyId: Int,
    val passedUserIds: Set<Long>
)
