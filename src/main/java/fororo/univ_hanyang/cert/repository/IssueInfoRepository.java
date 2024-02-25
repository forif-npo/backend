package fororo.univ_hanyang.cert.repository;

import fororo.univ_hanyang.cert.entity.IssueInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueInfoRepository extends JpaRepository<IssueInfo, Integer> {

    Optional<IssueInfo> findById_StudyId(Integer studyId);
    Optional<IssueInfo> findById_UserId(Integer userId);
}