package junsang.cho.catchpro.user.application.repository;

import junsang.cho.catchpro.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    boolean existsByEmail(String email);
}