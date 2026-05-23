package junsang.cho.catchpro.jwt.application.service;

import junsang.cho.catchpro.jwt.infrastructure.UserPrincipal;
import junsang.cho.catchpro.user.application.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.getUser(email)
                .map(UserPrincipal::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
