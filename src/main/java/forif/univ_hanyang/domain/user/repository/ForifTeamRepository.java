package forif.univ_hanyang.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.user.entity.ForifTeam;

import java.util.List;
import java.util.Optional;

public interface ForifTeamRepository extends JpaRepository<ForifTeam, Long> {
    Optional<List<ForifTeam>> findAllById_ActYearAndId_ActSemester(Integer actYear, Integer actSemester);
    Optional<ForifTeam> findById_UserId(Long id);
    void deleteAllById_UserId(Long userId);
}
