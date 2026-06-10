package junsang.cho.catchpro.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import junsang.cho.catchpro.jwt.infrastructure.JwtTokenProvider;
import junsang.cho.catchpro.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
// HTTP 요청에서 JWT 토큰을 추출하고 유효성을 검증, 인증 성공시 SecurityContextHolder에 인증 정보를 저장
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService; // 로그아웃(블랙리스트) 검증용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {

            //Redis 블랙리스트에 등록된(로그아웃된) 토큰인지 확인
            if (redisService.hasKey(token)) {
                log.info("이미 로그아웃 처리된(블랙리스트) 토큰으로 접근 시도 발생");
            } else {
                //토큰 인증
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // SecurityContext에 유저 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 유저 인증 정보를 저장했습니다.", authentication.getName());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 문자열 반환
        }
        return null; // JWT가 없으면 null 반환
    }
}