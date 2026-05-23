package junsang.cho.catchpro.user.application.factory;


import junsang.cho.catchpro.user.application.dto.command.JoinCommand;
import junsang.cho.catchpro.user.domain.User;
import junsang.cho.catchpro.user.domain.UserStatus;

public class UserFactory {

    public static User create(JoinCommand request, String encodedPassword) {
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();
    }
}
