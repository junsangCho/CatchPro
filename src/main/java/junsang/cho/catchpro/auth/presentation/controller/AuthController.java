package junsang.cho.catchpro.auth.presentation.controller;

import jakarta.servlet.http.HttpServletRequest;
import junsang.cho.catchpro.auth.application.service.AuthService;
import junsang.cho.catchpro.auth.presentation.dto.request.LoginRequest;
import junsang.cho.catchpro.auth.presentation.dto.response.LoginResponse;
import junsang.cho.catchpro.common.dto.response.CommonResponse;
import junsang.cho.catchpro.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequest request){
        var tokens = authService.authenticateUser(request);

        var user = userService.getUser(request.getLoginId());
        var response = new LoginResponse(user, tokens);

        return CommonResponse.success(response);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public CommonResponse<?> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        var tokens = authService.reissue(refreshToken);
        return CommonResponse.success(tokens); // 새 토큰 세트 반환
    }

    // 로그아웃
    @PostMapping("/logout")
    public CommonResponse<?> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return CommonResponse.success("로그아웃 되었습니다.");
    }

    // 헤더에서 Bearer 토큰 추출하는 유틸 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
