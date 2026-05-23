package junsang.cho.catchpro.auth.presentation.controller;

import junsang.cho.catchpro.auth.application.service.AuthService;
import junsang.cho.catchpro.auth.presentation.dto.request.LoginRequest;
import junsang.cho.catchpro.auth.presentation.dto.response.LoginResponse;
import junsang.cho.catchpro.common.dto.response.CommonResponse;
import junsang.cho.catchpro.jwt.infrastructure.JwtTokenProvider;
import junsang.cho.catchpro.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequest request){
        var userDetails = authService.authenticateUser(request);
        String token = jwtTokenProvider.generateToken(userDetails);
        var user = userService.getUser(userDetails.getUsername());
        var response = new LoginResponse(user, token);

        return CommonResponse.success(response);
    }
}
