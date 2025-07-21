package forif.univ_hanyang.domain.apply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.apply.entity.Apply;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Apply.ApplyId> {
    Optional<Apply> findById_ApplierId(Long applierId);

    Optional<Apply> findById_ApplierIdAndId_ApplyYearAndId_ApplySemester(Long applierId, Integer applyYear, Integer applySemester);

    List<Apply> findById_ApplyYearAndId_ApplySemesterAndPayStatus(
            Integer applyYear, Integer applySemester, Integer payStatus);

    List<Apply> findById_ApplyYearAndId_ApplySemester(Integer applyYear, Integer applySemester);

    Optional<Apply> findFirstById_ApplierIdOrderByApplyDateDesc(Long applierId);

    void deleteById_ApplierId(Long applierId);

    void deleteAll();
}