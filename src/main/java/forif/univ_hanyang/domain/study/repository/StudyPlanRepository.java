package forif.univ_hanyang.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.study.entity.StudyPlan;

import java.util.List;
import java.util.Optional;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, StudyPlan.StudyPlanId> {
    void deleteAllByStudy_Id(Integer studyId);
    Optional<List<StudyPlan>> findAllById_StudyIdOrderById_WeekNum(Integer studyId);
}
