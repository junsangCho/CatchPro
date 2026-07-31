package junsang.cho.catchpro.auth.presentation.dto.response;

import junsang.cho.catchpro.auth.presentation.result.Tokens;
import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginResponse {
    private final Long loginId;
    private final String name;
    private final String email;
    private final String token;
    private final String refreshToken;


    public LoginResponse(UserInfo userInfo, Tokens tokens) {
        this.loginId = userInfo.getId();
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
        this.token = tokens.getToken();
        this.refreshToken = tokens.getRefreshToken();
    }
}
