package junsang.cho.catchpro.user.domain.repository;


import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserInfo> getUser(String email);
}
