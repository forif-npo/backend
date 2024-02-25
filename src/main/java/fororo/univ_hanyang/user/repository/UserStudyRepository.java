package fororo.univ_hanyang.user.repository;

import fororo.univ_hanyang.user.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Integer> {

    List<UserStudy> findAllById_StudyId(Integer studyId);
    void deleteAllById_StudyId(Integer studyId);

    void deleteById_StudyId(Integer studyId);

    void deleteAllById_UserId(Integer userId);

    List<UserStudy> findAllById_UserId(Integer userId);

    Optional<UserStudy> findRecentUserStudyById_UserId(Integer userId);

    UserStudy findById_StudyIdAndId_UserId(Integer studyId, Integer userId);

}
