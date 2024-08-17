package forif.univ_hanyang.user.repository;

import forif.univ_hanyang.user.entity.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyUserRepository extends JpaRepository<StudyUser, Integer> {
    List<StudyUser> findAllById_StudyId(Integer studyId);
    void deleteAllById_StudyId(Integer studyId);

    void deleteById_StudyIdAndId_UserId(Integer studyId, Integer userId);

    void deleteAllById_UserId(Integer userId);

    List<StudyUser> findAllById_UserId(Integer userId);

    StudyUser findById_StudyIdAndId_UserId(Integer studyId, Integer userId);

}
