package com.duoc.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("test") // Este perfil se cargará solo en el entorno de pruebas
public class SecurityTestConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf
                .disable()) // Desactiva CSRF para pruebas
            .authorizeHttpRequests( authz -> authz
                .anyRequest().permitAll()); 
        return http.build();
    }

    @PostConstruct
    public void init() {
        System.out.println("Cargando configuración de seguridad para el perfil 'test'");
    }
}
