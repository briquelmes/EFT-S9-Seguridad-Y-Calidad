package com.duoc.backend.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void authenticateAcceptsBCryptPassword() {
        String username = UUID.randomUUID().toString();
        String rawPassword = UUID.randomUUID().toString();
        User user = userWithPassword(username, passwordEncoder.encode(rawPassword));
        when(userRepository.findByUsername(username)).thenReturn(user);

        MyUserDetailsService service = new MyUserDetailsService(userRepository, passwordEncoder);

        assertTrue(service.authenticate(username, rawPassword));
        verify(userRepository, never()).save(user);
    }

    @Test
    void authenticateUpgradesLegacyPlaintextPassword() {
        String username = UUID.randomUUID().toString();
        String rawPassword = UUID.randomUUID().toString();
        User user = userWithPassword(username, rawPassword);
        when(userRepository.findByUsername(username)).thenReturn(user);

        MyUserDetailsService service = new MyUserDetailsService(userRepository, passwordEncoder);

        assertTrue(service.authenticate(username, rawPassword));
        assertTrue(passwordEncoder.matches(rawPassword, user.getPassword()));
        verify(userRepository).save(user);
    }

    @Test
    void authenticateRejectsInvalidPassword() {
        String username = UUID.randomUUID().toString();
        String storedPassword = UUID.randomUUID().toString();
        User user = userWithPassword(username, passwordEncoder.encode(storedPassword));
        when(userRepository.findByUsername(username)).thenReturn(user);

        MyUserDetailsService service = new MyUserDetailsService(userRepository, passwordEncoder);

        assertFalse(service.authenticate(username, UUID.randomUUID().toString()));
        verify(userRepository, never()).save(user);
    }

    @Test
    void authenticateRejectsUnknownUser() {
        String unknownUsername = UUID.randomUUID().toString();
        when(userRepository.findByUsername(unknownUsername)).thenReturn(null);

        MyUserDetailsService service = new MyUserDetailsService(userRepository, passwordEncoder);

        assertFalse(service.authenticate(unknownUsername, UUID.randomUUID().toString()));
    }

    private User userWithPassword(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
