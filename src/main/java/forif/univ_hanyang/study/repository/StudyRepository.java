package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.user.dto.response.AllUserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study, Integer> {
    Optional<List<Study>> findAllByActYearAndActSemester(Integer act_year, Integer act_semester);
    @Query(value = "SELECT MAX(study_id) FROM tb_study", nativeQuery = true)
    Optional<Integer> findMaxStudyId();
}
