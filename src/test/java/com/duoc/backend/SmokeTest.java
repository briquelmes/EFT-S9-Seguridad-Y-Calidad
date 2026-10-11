package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LoginControllerTest {

    @Autowired
    private LoginController loginController;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @Test
    void testLoginControllerCreation() {
        // Validar que el controlador se haya creado correctamente
        assertNotNull(loginController, "El controlador LoginController no debería ser nulo");

        // Validar que las dependencias se hayan inyectado correctamente
        assertNotNull(userDetailsService, "El servicio MyUserDetailsService no debería ser nulo");
        assertNotNull(jwtAuthenticationConfig, "La configuración JWTAuthenticationConfig no debería ser nula");
    }
}