package forif.univ_hanyang.study.repository;

import forif.univ_hanyang.study.entity.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StudyRepository extends JpaRepository<Study,Integer> {
    List<Study> findAllByClubId(Integer clubId);
    Optional<Study> findByName(String name);
    Optional<Study> findByMentorIdAndClubId(Integer mentorId, Integer clubId);
    @EntityGraph(attributePaths = {"mentor", "weeklyPlans"})
    @Query("SELECT s FROM Study s WHERE s.clubId = :clubId")
    List<Study> findAllByClubIdWithDetails(@Param("clubId") Integer clubId);

}
