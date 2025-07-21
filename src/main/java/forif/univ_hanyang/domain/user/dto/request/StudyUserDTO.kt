package forif.univ_hanyang.domain.user.dto.request

data class StudyUserDTO(
    val studyId: Int,
    val passedUsers: List<PassedUserRequest>
)
