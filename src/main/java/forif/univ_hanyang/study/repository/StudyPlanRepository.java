package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    void deleteAllByStudy_Id(Integer studyId);
    Optional<List<StudyPlan>> findAllById_StudyIdOrderById_WeekNum(Integer studyId);
}
