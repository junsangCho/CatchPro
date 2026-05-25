package junsang.cho.catchpro.jwt.infrastructure;

import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {
    public UserPrincipal(UserInfo user) {
        super(user.getEmail(), user.getPassword(), user.isActive(), true, true, true, List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
