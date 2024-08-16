package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.MentorStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorStudyRepository extends JpaRepository<MentorStudy, Integer> {
    List<MentorStudy> findAllById_MentorId(Integer mentorId);
    void deleteAllById_StudyId(Integer studyId);

}
