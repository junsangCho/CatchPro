package junsang.cho.catchpro.auth.presentation.dto.response;

import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginResponse {
    private final Long loginId;
    private final String name;
    private final String token;


    public LoginResponse(UserInfo userInfo, String token) {
        this.loginId = userInfo.getId();
        this.name = userInfo.getName();
        this.token = token;
    }
}
