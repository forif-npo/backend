package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study, Integer> {
    Optional<List<Study>> findAllByActYearAndActSemester(Integer act_year, Integer act_semester);
}
