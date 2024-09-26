package forif.univ_hanyang.user.repository;

import forif.univ_hanyang.user.dto.response.AllUserInfoResponse;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {
    List<StudyUser> findAllById_StudyId(Integer studyId);
    void deleteAllById_StudyId(Integer studyId);
    void deleteById_StudyIdAndId_UserId(Integer studyId, Long userId);
    void deleteAllById_UserId(Long userId);
    List<StudyUser> findAllById_UserId(Long userId);
    List<StudyUser> findAllById_StudyIdIn(Set<Integer> studyIds);
    StudyUser findById_StudyIdAndId_UserId(Integer studyId, Long userId);
    List<StudyUser> findByUser(User user);

    @Query("SELECT new forif.univ_hanyang.user.dto.response.AllUserInfoResponse(u.id, u.name, u.email, u.phoneNumber, u.department, s.name) " +
            "FROM StudyUser su " +
            "JOIN su.user u " +
            "JOIN su.study s " +
            "WHERE s.actYear = :year AND s.actSemester = :semester")
    List<AllUserInfoResponse> getUserInfoByYearAndSemester(@Param("year") Integer year, @Param("semester") Integer semester);
}
