package forif.univ_hanyang.apply.repository;

import forif.univ_hanyang.apply.entity.StudyApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyApplyRepository extends JpaRepository<StudyApply, Integer> {
}
