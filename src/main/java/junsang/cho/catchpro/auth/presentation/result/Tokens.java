package junsang.cho.catchpro.auth.presentation.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Tokens {
    private final String token;
    private final String refreshToken;
}
