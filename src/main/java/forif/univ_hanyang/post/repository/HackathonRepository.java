package forif.univ_hanyang.post.repository;

import forif.univ_hanyang.post.domain.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HackathonRepository extends JpaRepository<Hackathon, Integer> {
    List<Hackathon> findAllByHeldYearAndHeldSemester(Integer heldYear, Integer heldSemester);
}
