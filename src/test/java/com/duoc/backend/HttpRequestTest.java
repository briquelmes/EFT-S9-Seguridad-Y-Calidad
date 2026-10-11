package com.duoc.backend;

import com.duoc.backend.user.User;
import com.duoc.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void ensureTestUserExists() {
        User testUser = userRepository.findByUsername("prueba");
        if (testUser == null) {
            testUser = new User();
            testUser.setUsername("prueba");
        }
        testUser.setPassword("123456");
        testUser.setEmail("prueba@example.com");
        testUser.setEnabled(true);
        userRepository.save(testUser);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        String baseUrl = "http://localhost:" + port + "/login";
        User loginRequest = new User();
        loginRequest.setUsername("prueba");
        loginRequest.setPassword("123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String token = Objects.requireNonNull(response.getBody(), "Login response body should not be null");
        assertTrue(token.startsWith("Bearer "));
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange
        String baseUrl = "http://localhost:" + port + "/login";
        User loginRequest = new User();
        loginRequest.setUsername("prueba");
        loginRequest.setPassword("1234567");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
