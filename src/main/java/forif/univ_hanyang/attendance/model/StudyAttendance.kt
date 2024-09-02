package forif.univ_hanyang.attendance.model

import forif.univ_hanyang.study.entity.Study
import forif.univ_hanyang.user.entity.User
import jakarta.persistence.*

@Entity
data class StudyAttendance(
    @EmbeddedId
    val id: StudyAttendanceId,

    @ManyToOne
    @MapsId("studyId")
    @JoinColumn(name = "study_id")
    val study: Study,

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    val attendanceStatus: AttendanceStatus
){
    constructor() : this(
        id =StudyAttendanceId(),
        study = Study(),
        user = User(),
        attendanceStatus = AttendanceStatus.결석)

    val weekNum: Int
        get() = id.weekNum
}


