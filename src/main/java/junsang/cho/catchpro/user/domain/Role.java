package junsang.cho.catchpro.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("일반유저")
    , ROLE_EXPERT("전문가");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
