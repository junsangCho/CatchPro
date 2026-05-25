package junsang.cho.catchpro.user.presentation.controller;

import jakarta.validation.Valid;
import junsang.cho.catchpro.auth.presentation.dto.request.JoinRequest;
import junsang.cho.catchpro.common.dto.response.CommonResponse;
import junsang.cho.catchpro.user.application.dto.command.JoinCommand;
import junsang.cho.catchpro.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public CommonResponse<?> joinUser(@Valid @RequestBody JoinRequest request) {
        var paramVO = request.toCommand();

        userService.joinUser(paramVO);

        return CommonResponse.success();
    }
}
