package forif.univ_hanyang.user.repository;

import forif.univ_hanyang.user.entity.ForifTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForifTeamRepository extends JpaRepository<ForifTeam, Long> {
    Optional<List<ForifTeam>> findAllById_ActYearAndId_ActSemester(Integer actYear, Integer actSemester);
    Optional<ForifTeam> findById_UserId(Long id);
    void deleteAllById_UserId(Long userId);
}
