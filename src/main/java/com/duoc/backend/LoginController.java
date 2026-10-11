package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.LoginRequest;

import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("login")
    public String login(@RequestBody LoginRequest loginRequest) {

        /**
        * En el ejemplo no se realiza la correcta validación del usuario
        */

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());

        if (!userDetails.getPassword().equals(loginRequest.password())) {
            throw new RuntimeException("Invalid login");
        }

        String token = jwtAuthenticationConfig.getJWTToken(loginRequest.username());
        return token;
    }

}