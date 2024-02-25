package fororo.univ_hanyang.study.repository;

import fororo.univ_hanyang.study.entity.StudyTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyTagRepository extends JpaRepository<StudyTag, Integer> {
    void deleteByStudy_StudyId(Integer StudyId);
}
