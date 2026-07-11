package com.archi.car_dealership_backend.auth.service;

import com.archi.car_dealership_backend.auth.dto.AuthResponse;
import com.archi.car_dealership_backend.auth.dto.LoginRequest;
import com.archi.car_dealership_backend.auth.dto.RegisterRequest;
import com.archi.car_dealership_backend.auth.exception.DuplicateResourceException;
import com.archi.car_dealership_backend.auth.util.JwtUtil;
import com.archi.car_dealership_backend.entity.Role;
import com.archi.car_dealership_backend.entity.User;
import com.archi.car_dealership_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @Test
    void login_success_returnsToken() {

        LoginRequest request =
                new LoginRequest(
                        "archi@test.com",
                        "password123"
                );

        Authentication authentication =
                mock(Authentication.class);

        User user = User.builder()
                .email("archi@test.com")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(userRepository.findByEmail("archi@test.com"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(user))
                .thenReturn("mock-jwt");

        AuthResponse response =
                authService.login(request);

        assertThat(response.token())
                .isEqualTo("mock-jwt");
    }
    @Test
    void login_wrongPassword_throwsBadCredentialsException() {

        LoginRequest request =
                new LoginRequest(
                        "archi@test.com",
                        "wrongPassword"
                );

        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new BadCredentialsException(
                                "Bad credentials"
                        )
                );

        assertThatThrownBy(() ->
                authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
    @Test
    void register_success_savesHashedPasswordAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("Archi", "archi@test.com", "password123");
        when(userRepository.existsByEmail("archi@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_pw");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("mock-jwt");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("mock-jwt");
        verify(userRepository).save(argThat(u -> u.getPassword().equals("hashed_pw")));
    }

    @Test
    void register_throwsDuplicateResourceException_whenEmailExists() {
        RegisterRequest request = new RegisterRequest("Archi", "dup@test.com", "password123");
        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already registered");

        verify(userRepository, never()).save(any());
    }
}
