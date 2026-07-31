package junsang.cho.catchpro.auth.application.service;

import junsang.cho.catchpro.auth.presentation.dto.request.LoginRequest;
import junsang.cho.catchpro.auth.presentation.result.Tokens;
import junsang.cho.catchpro.jwt.infrastructure.JwtTokenProvider;
import junsang.cho.catchpro.jwt.infrastructure.UserPrincipal;
import junsang.cho.catchpro.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Value("${app.jwt.refresh-expiration}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public Tokens authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword()));

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // UserUid 꺼내기
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String userUid = principal.getUserUid();

        // 🔥 Refresh Token을 Redis에 저장 (RT:{userUid})
        redisService.setValues("RT:" + userUid, refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return new Tokens(accessToken, refreshToken);
    }

    // 토큰 재발급
    public Tokens reissue(String refreshToken) {
        // 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }

        // 토큰에서 UserUid 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String userUid = Objects.requireNonNull(principal).getUserUid();

        // Redis에 저장된 Refresh Token 검증
        String redisRefreshToken = redisService.getValues("RT:" + userUid);
        if (ObjectUtils.isEmpty(redisRefreshToken) || !redisRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("토큰 정보가 일치하지 않습니다. 다시 로그인해주세요.");
        }

        // 신규 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // Redis 토큰 정보 업데이트
        redisService.setValues("RT:" + userUid, newRefreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return new Tokens(newAccessToken, newRefreshToken);
    }

    // 3. 로그아웃 로직
    public void logout(String bearerToken) {
        String accessToken = jwtTokenProvider.resolveToken(bearerToken);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String userUid = Objects.requireNonNull(principal).getUserUid();

        // Redis에서 Refresh Token 삭제
        if (redisService.hasKey("RT:" + userUid)) {
            redisService.deleteValues("RT:" + userUid);
        }

        // Access Token을 블랙리스트에 등록(남은 수명만큼만!)
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisService.setValues("AT:" + accessToken, "logout", Duration.ofMillis(expiration));

        log.info("로그아웃 성공 - userUid: {}", userUid);
    }
}
