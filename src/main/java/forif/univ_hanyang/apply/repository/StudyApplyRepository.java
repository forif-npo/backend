package forif.univ_hanyang.apply.repository;

import forif.univ_hanyang.apply.entity.StudyApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyApplyRepository extends JpaRepository<StudyApply, Integer> {
    @Query("SELECT DISTINCT sa FROM StudyApply sa " +
            "LEFT JOIN FETCH sa.primaryMentor " +
            "LEFT JOIN FETCH sa.secondaryMentor " +
            "LEFT JOIN FETCH sa.studyApplyPlans " +
            "WHERE sa.id IN :ids")
    List<StudyApply> findAllWithMentorsById(@Param("ids") List<Integer> ids);
}
