package fororo.univ_hanyang.clubInfo.repository;

import fororo.univ_hanyang.clubInfo.entity.ClubInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubInfoRepository extends JpaRepository<ClubInfo, Integer> {
    Optional<ClubInfo> findByActivityYearAndActivitySemester(Integer activityYear, Integer activitySemester);
}
