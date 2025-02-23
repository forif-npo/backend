package forif.univ_hanyang.apply.repository;

import forif.univ_hanyang.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Apply.ApplyId> {
    Optional<Apply> findById_ApplierId(Long applierId);

    Optional<Apply> findById_ApplierIdAndId_ApplyYearAndId_ApplySemester(Long applierId, Integer applyYear, Integer applySemester);

    void deleteById_ApplierId(Long applierId);

    void deleteAll();
}