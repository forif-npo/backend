package forif.univ_hanyang.user.repository;

import forif.univ_hanyang.user.entity.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {
    List<StudyUser> findAllById_StudyId(Integer studyId);
    void deleteAllById_StudyId(Integer studyId);
    Optional<StudyUser> findById_StudyId(Integer studyId);
    void deleteById_StudyIdAndId_UserId(Integer studyId, Long userId);

    void deleteAllById_UserId(Long userId);

    List<StudyUser> findAllById_UserId(Long userId);

    StudyUser findById_StudyIdAndId_UserId(Integer studyId, Long userId);
}
