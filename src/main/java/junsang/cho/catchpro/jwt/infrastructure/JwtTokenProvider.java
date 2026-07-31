package junsang.cho.catchpro.jwt.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION_TIME;

    @Value("${app.jwt.refresh-expiration}")
    private long REFRESH_EXPIRATION_TIME;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    // 1. Access Token 생성 (짧은 수명)
    public String createAccessToken(Authentication authentication) {
        return buildToken(authentication, EXPIRATION_TIME);
    }

    // 2. Refresh Token 생성 (긴 수명)
    public String createRefreshToken(Authentication authentication) {
        return buildToken(authentication, REFRESH_EXPIRATION_TIME);
    }

    // 공통 토큰 빌더 로직
    private String buildToken(Authentication authentication, long expirationTime) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 권한 정보 문자열 변환 (예: "ROLE_USER,ROLE_EXPERT")
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName()) // username 세팅
                .claim("principal", principal.getUserUid()) // 권한 세팅
                .claim("authorities", authorities) // 권한 세팅
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).get("username").toString();
    }

    // 토큰의 유효성을 검사
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 유효하지 않은 토큰
        }
    }

    // 토큰에서 Claims 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // JwtParserBuilder 사용
                .setSigningKey(key) // 서명 키 설정
                .build() // 빌드
                .parseClaimsJws(token) // 토큰 파싱
                .getBody(); // Claims 반환
    }

    // 토큰에서 Authentication 객체 추출 (Filter에서 사용)
    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // Security User 객체 생성
        UserPrincipal principal = new UserPrincipal(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // Redis 블랙리스트용: 토큰의 남은 만료 시간 계산
    public long getExpiration(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        long now = System.currentTimeMillis();
        return Math.max(0, expiration.getTime() - now); // 이미 만료되었으면 0 반환
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 문자열 반환
        }
        return null; // JWT가 없으면 null 반환
    }
}
