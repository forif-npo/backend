package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study,Integer> {
    Optional<Study> findByName(String name);
    Optional<List<Study>> findAllByActYearAndActSemester(Integer act_year, Integer act_semester);
    @Query(value = "SELECT MAX(study_id) FROM tb_study", nativeQuery = true)
    Optional<Integer> findMaxStudyId();
}
