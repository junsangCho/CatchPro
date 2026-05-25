package junsang.cho.catchpro.user.application.dto.command;

import junsang.cho.catchpro.auth.presentation.dto.request.JoinRequest;
import junsang.cho.catchpro.user.domain.Role;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class JoinCommand {

    private final String email;
    private final String password;
    private final String name;
    private final Role role;



}
