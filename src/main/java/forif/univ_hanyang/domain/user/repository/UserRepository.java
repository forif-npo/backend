package forif.univ_hanyang.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import forif.univ_hanyang.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
