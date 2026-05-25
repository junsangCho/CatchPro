package junsang.cho.catchpro.user.application.service;

import junsang.cho.catchpro.user.application.dto.command.JoinCommand;
import junsang.cho.catchpro.user.application.factory.UserFactory;
import junsang.cho.catchpro.user.infrastructure.persistence.UserJpaRepository;
import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;


    public void joinUser(JoinCommand request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userExists(request.getEmail());

        userJpaRepository.save(UserFactory.create(request, encodedPassword));
    }

    public UserInfo getUser(String email){
        return userJpaRepository.getUser(email).orElseThrow();
    }

    public void userExists(String email) {
        if(userJpaRepository.existsByEmail(email)){
            throw new IllegalArgumentException();
        }
    }
}
