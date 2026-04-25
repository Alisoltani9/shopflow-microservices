package soltani.code.user_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soltani.code.user_service.dto.LoginRequest;
import soltani.code.user_service.dto.RegisterRequest;
import soltani.code.user_service.entity.User;
import soltani.code.user_service.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")

    public User register(@Valid @RequestBody RegisterRequest request)
    {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request)
    {
        return userService.login(request);
    }


}
