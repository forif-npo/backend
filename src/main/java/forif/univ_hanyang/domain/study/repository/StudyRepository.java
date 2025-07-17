package forif.univ_hanyang.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.study.entity.Study;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study, Integer> {
    Optional<List<Study>> findAllByActYearAndActSemester(Integer act_year, Integer act_semester);
}
