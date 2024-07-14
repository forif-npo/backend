package forif.univ_hanyang.cert.repository;

import forif.univ_hanyang.cert.entity.CertJson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertJsonRepository extends JpaRepository<CertJson,String> {
    Optional<CertJson> findByCertJsonId(String certJsonId);
}
