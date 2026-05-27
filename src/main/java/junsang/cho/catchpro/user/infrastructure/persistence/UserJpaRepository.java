package junsang.cho.catchpro.user.infrastructure.persistence;

import junsang.cho.catchpro.user.domain.User;
import junsang.cho.catchpro.user.domain.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    boolean existsByEmail(String email);
}