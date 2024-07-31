package forif.univ_hanyang.apply.repository;

import forif.univ_hanyang.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Integer> {
    Optional<Apply> findByApplierId(Integer applierId);
    List<Apply> findAllByPayYn(String payYn);
    List<Apply> findAllByPrimaryStudy(Integer primary);
    List<Apply> findAllBySecondaryStudy(Integer secondary);
    void deleteByApplierId(Integer applierId);
    void deleteAll();

}
