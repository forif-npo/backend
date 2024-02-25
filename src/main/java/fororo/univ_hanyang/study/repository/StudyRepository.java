package fororo.univ_hanyang.study.repository;

import fororo.univ_hanyang.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study,Integer> {
    List<Study> findAllByClubId(Integer clubId);
    Optional<Study> findByStudyName(String studyName);
    Optional<Study> findByStudyId(Integer studyId);
    Optional<Study> findByMentorIdAndClubId(Integer mentorId, Integer clubId);

}
