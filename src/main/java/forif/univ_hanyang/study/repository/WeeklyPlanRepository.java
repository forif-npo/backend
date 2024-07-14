package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Integer> {
    void deleteAllByStudy_Id(Integer studyId);
    List<WeeklyPlan> findAllByStudy_IdOrderByPlanId(Integer studyId);
}
