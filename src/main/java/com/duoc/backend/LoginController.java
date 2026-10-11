package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.LoginRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private final JWTAuthenticationConfig jwtAuthenticationConfig;
    private final MyUserDetailsService userDetailsService;

    public LoginController(JWTAuthenticationConfig jwtAuthenticationConfig, MyUserDetailsService userDetailsService) {
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(@RequestBody LoginRequest loginRequest) {
        if (!userDetailsService.authenticate(loginRequest.username(), loginRequest.password())) {
            throw new RuntimeException("Invalid login");
        }

        String token = jwtAuthenticationConfig.getJWTToken(loginRequest.username());
        return token;
    }

}