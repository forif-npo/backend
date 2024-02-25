package fororo.univ_hanyang.apply.repository;

import fororo.univ_hanyang.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Integer> {
    Optional<Apply> findByApplierId(Integer applierId);
    List<Apply> findAllByIsPaidFalse();
    List<Apply> findAllByPrimaryStudyOrSecondaryStudy(String primary, String secondary);
    void deleteByApplierId(Integer applierId);
    void deleteAll();

}
