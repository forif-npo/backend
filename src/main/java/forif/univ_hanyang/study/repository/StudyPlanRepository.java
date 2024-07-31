package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    void deleteAllByStudy_Id(Integer studyId);
    List<StudyPlan> findAllById_StudyIdOrderById_WeekNum(Integer studyId);
}
