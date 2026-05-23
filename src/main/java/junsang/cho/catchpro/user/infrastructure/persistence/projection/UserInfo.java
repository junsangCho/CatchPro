package junsang.cho.catchpro.user.infrastructure.persistence.projection;

import junsang.cho.catchpro.user.domain.Role;
import junsang.cho.catchpro.user.domain.User;
import junsang.cho.catchpro.user.domain.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String email;
    private String password;
    private String name;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserInfo(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public boolean isActive(){
        return this.status.equals(UserStatus.ACTIVE);
    }
}