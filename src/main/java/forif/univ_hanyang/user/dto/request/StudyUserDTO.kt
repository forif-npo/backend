package forif.univ_hanyang.user.dto.request

data class StudyUserDTO(
    val studyId: Int,
    val passedUsers: List<PassedUserRequest>
)
