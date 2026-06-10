package junsang.cho.catchpro.jwt.infrastructure;


import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final String userUid; // 핵심 식별자 추가!
    private final String loginId;
    private final String password;
    private final boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 사용자 인증 후 사용자 데이터를 담을 생성자
     * @param user 사용자 정보 객체
     */
    public UserPrincipal(UserInfo user) {
        this.userUid = user.getUserUid();
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.isActive = user.isActive();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * filter에서 토큰 인증후 가볍게 사용하는 생성자
     * @param userUid
     * @param loginId
     * @param authorities
     */
    public UserPrincipal(String userUid, String loginId, Collection<? extends GrantedAuthority> authorities) {
        this.userUid = userUid;
        this.loginId = loginId;
        this.password = ""; // 토큰 검증이 끝났으므로 비밀번호는 필요 없습니다.
        this.isActive = true;
        this.authorities = authorities;
    }

    // --- UserDetails 필수 오버라이드 메서드들 ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return loginId; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return isActive; }

    public Object getUserUid() {
        return userUid;
    }
}
