package forif.univ_hanyang.domain.attendance.model

import forif.univ_hanyang.domain.study.entity.Study
import forif.univ_hanyang.domain.user.entity.User
import jakarta.persistence.*
import java.sql.Date
import java.time.LocalDate
import java.time.MonthDay

@Entity
@Table(name = "tb_study_attendance")
data class StudyAttendance(
    @EmbeddedId
    val id: StudyAttendanceId,

    val studyDate: String? = null,

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
        studyDate = null,
        study = Study(),
        user = User(),
        attendanceStatus = AttendanceStatus.결석)
}


