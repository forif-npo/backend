package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.MentorStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorStudyRepository extends JpaRepository<MentorStudy, Long> {
    List<MentorStudy> findAllById_MentorId(Long mentorId);
    void deleteAllById_StudyId(Integer studyId);
    void deleteAllById_MentorId(Long mentorId);

}
