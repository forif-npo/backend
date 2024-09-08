package forif.univ_hanyang.apply.repository;

import forif.univ_hanyang.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    Optional<Apply> findByApplierId(Long applierId);
    void deleteByApplierId(Long applierId);
    void deleteAll();

}
