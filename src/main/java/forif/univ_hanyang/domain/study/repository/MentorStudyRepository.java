package forif.univ_hanyang.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.study.entity.MentorStudy;

import java.util.List;

public interface MentorStudyRepository extends JpaRepository<MentorStudy, Long> {
    List<MentorStudy> findAllById_MentorId(Long mentorId);
    void deleteAllById_StudyId(Integer studyId);
    void deleteAllById_MentorId(Long mentorId);

}
