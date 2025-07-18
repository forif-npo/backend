package forif.univ_hanyang.domain.apply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.apply.entity.StudyApplyPlan;

public interface StudyApplyPlanRepository extends JpaRepository<StudyApplyPlan, StudyApplyPlan.StudyApplyPlanId> {
}
