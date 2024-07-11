package fororo.univ_hanyang.study.repository;

import fororo.univ_hanyang.study.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Integer> {
    void deleteAllByStudy_StudyId(Integer studyId);
    List<WeeklyPlan> findAllByStudy_StudyIdOrderByPlanId(Integer studyId);
}
