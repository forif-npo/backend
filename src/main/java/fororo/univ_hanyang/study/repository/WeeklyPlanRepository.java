package fororo.univ_hanyang.study.repository;

import fororo.univ_hanyang.study.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Integer> {
    void deleteAllByStudy_StudyId(Integer studyId);
}
