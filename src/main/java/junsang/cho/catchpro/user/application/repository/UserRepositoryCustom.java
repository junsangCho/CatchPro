package junsang.cho.catchpro.user.application.repository;


import junsang.cho.catchpro.user.infrastructure.persistence.projection.UserInfo;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserInfo> getUser(String email);
}
