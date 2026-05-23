package junsang.cho.catchpro.user.application.service;

import junsang.cho.catchpro.user.application.dto.command.JoinCommand;
import junsang.cho.catchpro.user.application.factory.UserFactory;
import junsang.cho.catchpro.user.application.repository.UserRepository;
import junsang.cho.catchpro.user.infrastructure.persistence.projection.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void joinUser(JoinCommand request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userExists(request.getEmail());

        userRepository.save(UserFactory.create(request, encodedPassword));
    }

    public UserInfo getUser(String email){
        return userRepository.getUser(email).orElseThrow();
    }

    public void userExists(String email) {
        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException();
        }
    }
}
